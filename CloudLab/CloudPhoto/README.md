# Установка программы

Для установки выполните следующие скрипты:

```
mkdir -p ~/tmp/svidirov # создание временной директории для файлов
cd ~/tmp/svidirov # переход во временную директорию
wget -O cloud-photo-0.1.jar https://github.com/OneWayDream/University-tasks/blob/main/CloudLab/CloudPhoto/cloud-photo-0.1.jar?raw=true # скачивание исполняемого jar файла.
echo 'alias cloudphoto="java -jar ~/tmp/svidirov/cloud-photo-0.1.jar"' >> ~/.bashrc # создание алиаса для jar файла и его регистрация
source ~/.bashrc # перезагрузка списка алиасов для загрузки нового.
```

Для работы программы понадобится JRE. Проверить её наличие можно с помощью команды

```
java --version
```

Если JRE не установлена, воспользуйтесь командой
```
sudo apt install openjdk-17-jre
```
