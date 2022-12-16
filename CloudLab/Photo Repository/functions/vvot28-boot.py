import json
import boto3
from boto3.dynamodb.conditions import Attr
import requests
import os

YDB_ENDPOINT = os.environ['YDB_ENDPOINT']
TABLE_NAME = os.environ['TABLE_NAME']
PICTURES_GATEWAY_URL = os.environ['PICTURES_GATEWAY_URL']
PHOTOS_GATEWAY_PATH = os.environ['PHOTOS_GATEWAY_PATH']
FACES_GATEWAY_PATH = os.environ['FACES_GATEWAY_PATH']
BOT_ID = os.environ['BOT_ID']
BOT_TOKEN = os.environ['BOT_TOKEN']
TELEGRAM_URL = 'https://api.telegram.org/bot{}/{}'
SEND_BATCH_PICTURES_METHOD_NAME = 'sendMediaGroup'
SEND_PICTURE_METHOD_NAME = 'sendPhoto'


def handler(event, context):
    body = json.loads(event['body'])
    print('-----------')
    print(body['message'])
    print('-----------')
    response = {}
    if is_face_recognition_answer(body):
        response = handle_face_recognition_answer(body)
    elif 'text' in body['message']:
        text = body['message']['text']
        if text == '/getface':
            response = get_find_face_response(body)
        elif text.startswith('/find'):
            response = get_find_pictures_by_name_response(body)
        else:
            response = get_commands_response(body)
    else:
        response = get_commands_response(body)
    return response


def get_commands_response(body):
    return {
        'statusCode': 200,
        'headers': {
            'Content-Type': 'application/json'
        },
        'body': json.dumps({
            'method': 'sendMessage',
            'chat_id': body['message']['chat']['id'],
            'text': "Wrong command. Here are available commands:\n" +
                    "\t/getface - Get a face to face recognition.\n" +
                    "\t/find {find} - Find all the pictures of the person."
        }),
        'isBase64Encoded': False
    }


def get_find_face_response(body):
    # Создаём сессию к Yandex Cloud
    session = boto3.session.Session(region_name='ru-central1')

    # Создаём клиента для обращения к YDB
    ydb_client = session.resource('dynamodb', endpoint_url=YDB_ENDPOINT)

    # Достаём одну запись без имени
    table = ydb_client.Table(TABLE_NAME)
    response = table.scan(
        AttributesToGet=[
            'face_photo_key',
        ],
        FilterExpression=Attr("name").not_exists()
    )

    response_body = {}

    if len(response['Items']) > 0:
        image_id = response['Items'][0]['face_photo_key']
        response_body = json.dumps({
            'method': 'sendPhoto',
            'chat_id': body['message']['chat']['id'],
            'photo': PICTURES_GATEWAY_URL + FACES_GATEWAY_PATH + image_id,
            'caption': 'Hey, we\'ve got an unrecognised face ({}). Can you help us out:?'.format(image_id)
        })
    else:
        response_body = json.dumps({
            'method': 'sendMessage',
            'chat_id': body['message']['chat']['id'],
            'text': 'All persons are identified с:'
        })
    return {
        'statusCode': 200,
        'headers': {
            'Content-Type': 'application/json'
        },
        'body': response_body,
        'isBase64Encoded': False
    }


def get_find_pictures_by_name_response(body):
    name = body['message']['text'][6:]

    # Создаём сессию к Yandex Cloud
    session = boto3.session.Session(region_name='ru-central1')

    # Создаём клиента для обращения к YDB
    ydb_client = session.resource('dynamodb', endpoint_url=YDB_ENDPOINT)

    # Получаем все изображения по имени
    table = ydb_client.Table(TABLE_NAME)
    response = table.scan(
        AttributesToGet=[
            'source_photo_key',
        ],
        FilterExpression=Attr("name").eq(name)
    )
    data = [entry['source_photo_key'] for entry in response['Items']]

    response_body = {}

    if len(data) > 0:
        send_photos(body, data)
        response_body = {
            'statusCode': 200,
            'headers': {
                'Content-Type': 'application/json'
            },
            'body': json.dumps({
                'method': 'sendMessage',
                'chat_id': body['message']['chat']['id'],
                'text': 'All photos for \'{}\' have been sent'.format(name)
            }),
            'isBase64Encoded': False
        }
    else:
        response_body = get_no_photos_response(body)
    return response_body


def is_face_recognition_answer(body):
    return ('reply_to_message' in body['message']) and \
           (body['message']['reply_to_message']['from']['username']) == BOT_ID and \
           ('photo' in body['message']['reply_to_message']) and \
           ('caption' in body['message']['reply_to_message'])


def handle_face_recognition_answer(body):
    # Парсим ответ
    photo_caption = body['message']['reply_to_message']['caption']
    face_key = photo_caption[37:photo_caption.rindex('.jpg') + 4]
    recognized_person = body['message']['text']

    # Создаём сессию к Yandex Cloud
    session = boto3.session.Session(region_name='ru-central1')

    # Создаём клиента для обращения к YDB
    ydb_client = session.resource('dynamodb', endpoint_url=YDB_ENDPOINT)

    # Обновляем значение в бд
    table = ydb_client.Table(TABLE_NAME)
    table.update_item(
        Key={
            'face_photo_key': face_key
        },
        UpdateExpression="set name = :s",
        ExpressionAttributeValues={
            ':s': recognized_person
        }
    )

    return {
        'statusCode': 200,
        'headers': {
            'Content-Type': 'application/json'
        },
        'body': json.dumps({
            'method': 'sendMessage',
            'chat_id': body['message']['chat']['id'],
            'text': 'The data have been successfully updated.'
        }),
        'isBase64Encoded': False
    }


def send_photos(body, data):
    media_data = [{'type': 'photo', 'media': PICTURES_GATEWAY_URL + PHOTOS_GATEWAY_PATH + image_key} for image_key in
                  data]
    for i in range(10, len(media_data), 10):
        send_batch_photos(media_data[i - 10: i])
    remaining_entries = len(media_data) % 10
    if remaining_entries > 1:
        send_batch_photos(body, media_data[-remaining_entries:])
    else:
        send_photo(body, media_data[-remaining_entries:][0]['media'])


def send_batch_photos(body, data):
    request = requests.post(
        url=TELEGRAM_URL.format(BOT_TOKEN, SEND_BATCH_PICTURES_METHOD_NAME),
        headers={
            'Content-Type': 'application/json'
        },
        data=json.dumps({
            'chat_id': body['message']['chat']['id'],
            'media': data
        })
    )
    print(request.json())


def send_photo(body, data):
    request = requests.post(
        url=TELEGRAM_URL.format(BOT_TOKEN, SEND_PICTURE_METHOD_NAME),
        headers={
            'Content-Type': 'application/json'
        },
        data=json.dumps({
            'chat_id': body['message']['chat']['id'],
            'photo': data
        })
    )
    print(request.json())


def get_no_photos_response(body):
    return {
        'statusCode': 200,
        'headers': {
            'Content-Type': 'application/json'
        },
        'body': json.dumps({
            'method': 'sendMessage',
            'chat_id': body['message']['chat']['id'],
            'text': 'There are no pictures of this person here'
        }),
        'isBase64Encoded': False
    }
