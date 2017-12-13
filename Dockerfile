FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/genres-0.0.1-SNAPSHOT.jar /app

EXPOSE 8083

CMD ["java", "-jar", "genres-0.0.1-SNAPSHOT.jar"]