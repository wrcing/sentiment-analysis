#!/bin/bash


#修改yml配置，重启，有效果
#docker compose -f ./docker-compose.yml stop nginx
#docker compose -f ./docker-compose.yml up --build -d nginx

docker compose -f ./docker-compose.yml down
