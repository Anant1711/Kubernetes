FROM openjdk:17
ADD target/springboot-docker-compose.jar springboot-docker-compose.jar
ENTRYPOINT ["java","-jar","springboot-docker-compose.jar"]