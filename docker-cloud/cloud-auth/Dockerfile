FROM openjdk:8u312-jre-slim-buster
MAINTAINER wrcing@126.com

WORKDIR /data/wrcing/
COPY ./cloud-auth.jar /data/wrcing/

EXPOSE 2245

ENV APP_PORT=2245
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Xss256k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+UseCGroupMemoryLimitForHeap -XX:+HeapDumpOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
#ENV EXT_ARG="-Dserver.port=8080 -Dspring.cloud.nacos.config.enabled=false -Dspring.cloud.nacos.discovery.server-addr=svc-nacos:8848 -Dspring.cloud.nacos.discovery.namespace=public"
ENV EXT_ARG="-Dserver.port=2245 -Dspring.profiles.active=prod"

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT java  $JAVA_OPTS $EXT_ARG -Dserver.port=$APP_PORT -jar /data/wrcing/cloud-auth.jar
