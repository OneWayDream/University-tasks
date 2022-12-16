import boto3
from io import BytesIO
import base64
import requests
import json
import os


def handler(event, context):
    YANDEX_VISION_URL = 'https://vision.api.cloud.yandex.net/vision/v1/batchAnalyze'
    FOLDER_ID = os.environ['FOLDER_ID']
    QUEUE_NAME = os.environ['QUEUE_NAME']
    IMAGE_MIME_TYPE = 'image/jpeg'

    # Достаём информацию о местоположении файла
    bucket_id = event['messages'][0]['details']['bucket_id']
    object_id = event['messages'][0]['details']['object_id']

    # Создаём сессию к Yandex Cloud
    session = boto3.session.Session(region_name='ru-central1')
    s3_client = session.client(
        service_name='s3',
        endpoint_url='https://storage.yandexcloud.net'
    )

    # Скачиваем изображение
    image = BytesIO()
    s3_client.download_fileobj(bucket_id, object_id, image)

    # Шифруем изображение (требование Yandex Vision API)
    encoded_image = encode_file(image)

    # Достаём токен сервисного аккаунта
    token = context.token['access_token']

    # Создаём тело запроса
    body = json.dumps({
        "analyzeSpecs": [
            {
                "features": [
                    {
                        "type": "FACE_DETECTION",
                    }
                ],
                "mimeType": IMAGE_MIME_TYPE,
                "content": encoded_image.decode("UTF-8")
            }
        ],
        "folderId": FOLDER_ID
    })

    # Отправляем запрос на сервер и принимаем ответ
    request = requests.post(
        url=YANDEX_VISION_URL,
        headers={
            'Content-Type': 'application/json',
            'Authorization': 'Bearer {}'.format(token)
        },
        data=body
    )
    response_json = request.json()
    print(response_json)

    # Создаём клиента для работы с очередью
    sqs_client = boto3.client(
        service_name='sqs',
        endpoint_url='https://message-queue.api.cloud.yandex.net',
        region_name='ru-central1'
    )

    # Получаем url очереди
    queue_url = sqs_client.get_queue_url(QueueName=QUEUE_NAME)['QueueUrl']

    # Обрабатываем ответ
    if 'faces' in response_json['results'][0]['results'][0]['faceDetection']:
        for face in response_json['results'][0]['results'][0]['faceDetection']['faces']:
            # Каждое лицо отправляем в очередь
            face_bounds = face['boundingBox']['vertices']
            sqs_client.send_message(
                QueueUrl=queue_url,
                MessageBody=json.dumps({
                    'object-id': object_id,
                    'face-bounds': face_bounds
                })
            )
    else:
        print('No faces were found.')

    return {
        'statusCode': 200
    }


def encode_file(file):
    file_content = file.getbuffer()
    return base64.b64encode(file_content)
