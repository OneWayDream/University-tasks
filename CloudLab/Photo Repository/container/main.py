import os
from io import BytesIO
from sanic import Sanic
import sanic.response as response
import boto3
from PIL import Image
import json

app = Sanic(__name__)
VERSION = '1.20a'
PHOTOS_BUCKET_NAME = os.environ['PHOTOS_BUCKET_NAME']
FACES_BUCKET_NAME = os.environ['FACES_BUCKET_NAME']
YDB_ENDPOINT = os.environ['YDB_ENDPOINT']
TABLE_NAME = os.environ['TABLE_NAME']

# Создаём сессию к Yandex Cloud
session = boto3.session.Session(region_name='ru-central1')

# Создаём клиента для доступа к бакетам
s3_client = session.client(
    service_name='s3',
    endpoint_url='https://storage.yandexcloud.net'
)

# Создаём клиента для обращения к YDB
ydb_client = session.client('dynamodb', endpoint_url=YDB_ENDPOINT)


@app.after_server_start
async def after_server_start(app, loop):
    print('Version {}'.format(VERSION))
    print(f"App listening at port {os.environ['PORT']}")


@app.route("", methods=['GET', 'POST'])
async def cut_image(request):

    # Парсим и логируем сообщение
    message = request.json['messages'][0]['details']['message']
    body = json.loads(message['body'])
    print("---------------")
    print(body)
    print("---------------")
    # Достаём информацию о местоположении файла
    object_id = body['object-id']
    face_bounds = body['face-bounds']
    message_id = message['message_id']

    # Скачиваем изображение
    image_bytes = BytesIO()
    s3_client.download_fileobj(PHOTOS_BUCKET_NAME, object_id, image_bytes)

    # Считываем изображение в файл
    image = Image.open(image_bytes)

    # Вырезаем изображение
    left = int(face_bounds[0]['x'])
    top = int(face_bounds[0]['y'])
    right = int(face_bounds[2]['x'])
    bottom = int(face_bounds[2]['y'])
    cropped_image = image.crop((left, top, right, bottom))

    # Создаём уникальное для лица имя
    type_index = object_id.rindex('.')
    new_image_name = object_id[:type_index]
    image_type = object_id[type_index:]
    new_image_name = new_image_name + message_id + image_type

    # Конвертируем изображение в байты
    cropped_image_bytes = BytesIO()
    cropped_image.save(cropped_image_bytes, format='JPEG')

    # Сохраняем изображение в itis-2022-2023-vvot28-faces
    s3_client.put_object(Bucket=FACES_BUCKET_NAME, Key=new_image_name, Body=cropped_image_bytes.getvalue())

    new_entry = {
        'face_photo_key': {
            'S': new_image_name
        },
        'source_photo_key': {
            'S': object_id
        }
    }
    print("---------------")
    print('New entry: {}'.format(new_entry))
    print("---------------")
    ydb_client.put_item(TableName=TABLE_NAME, Item=new_entry)


    # Возвращаем положительный ответ (чтобы из очереди удалилось сообщение)
    return response.json(
        {'message': "Handled."},
        status=200
    )


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=int(os.environ['PORT']), motd=False, access_log=False)
