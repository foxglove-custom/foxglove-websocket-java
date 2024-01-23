### 基础镜像
FROM  openjdk:8-jdk

# 作者
MAINTAINER luyunqiang <yunqiang.lu@hirain.com>

# 系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

RUN mkdir -p /usr/local

# 应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar
COPY target/foxglove-websocket-*.jar /usr/local/app.jar
#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 启动容器时的进程
ENTRYPOINT ["java", "-jar","-Xms1024m","/usr/local/app.jar"]