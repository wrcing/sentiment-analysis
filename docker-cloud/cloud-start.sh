#!/bin/bash

echo "dos2unix 安装与格式化"
yum install -y dos2unix
dos2unix cloud-stop.sh
echo "=========== dos2unix格式化结束  ============"

echo "准备删除已有镜像："
delList=("cloud-auth" "cloud-sent-manage" "cloud-gateway" "none")
#docker images | grep "" | awk '{print $3}'| xargs -n 1 docker rmi $1
for i in ${delList[@]}; do
  echo $i
  imgIDs=""
  imgIDs=`docker images | grep $i | awk '{print $3}'`
  if test -z "$imgIDs"
  then
    echo "无images可删 $imgIDs"
  else
    docker rmi $imgIDs
    echo "已删除 $imgIDs"
  fi
done
echo "============== 删除已有镜像 结束 =============="

echo "mv .jar 到当前目录"
mv ../sentiment-analysis-manage/target/sentiment-analysis-manage.jar ./sentiment-analysis-manage
mv ../gateway9527/target/gateway9527.jar ./gateway9527
mv ../cloud-auth/target/cloud-auth.jar ./cloud-auth
echo "========= mv结束 ========="

docker compose -f ./docker-compose.yml up -d
