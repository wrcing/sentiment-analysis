#!/bin/bash

echo "dos2unix 安装与格式化"
yum install -y dos2unix
dos2unix cloud-stop.sh
echo "=========== dos2unix格式化结束  ============"

echo "mv .jar 到当前目录"
mv ../sentiment-analysis-manage/target/sentiment-analysis-manage.jar ./sentiment-analysis-manage
mv ../gateway9527/target/gateway9527.jar ./gateway9527
mv ../cloud-auth/target/cloud-auth.jar ./cloud-auth
echo "========= mv结束 ========="

docker compose -f ./docker-compose.yml up -d
