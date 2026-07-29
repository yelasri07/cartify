FROM localhost:8081/docker-hub/library/eclipse-temurin:17
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]