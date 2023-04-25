#!/bin/bash

yum install -y dos2unix
dos2unix docker-env-stop.sh

docker compose -f ./docker-compose.yml up -d

echo "开启rabbitmq的web:"
docker exec -it rabbitmq /bin/bash -c "rabbitmq-plugins enable rabbitmq_management"
echo "=====rabbitmq plugins end====="
