FROM python:3.10-slim

WORKDIR /app
RUN pip install --no-cache-dir --prefer-binary sanic
RUN pip install --no-cache-dir --prefer-binary boto3
RUN pip install --no-cache-dir --prefer-binary Pillow
COPY ./main.py .

CMD [ "python3", "main.py" ]