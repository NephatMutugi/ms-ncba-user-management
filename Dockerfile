FROM openjdk:17-jdk-slim

LABEL maintainer="muchirinephat5@gmail.com"

EXPOSE 8080

VOLUME /tmp

ADD target/app.jar app.jar

RUN /bin/sh -c 'touch /app.jar'

ENV TZ=Africa/Nairobi

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

