FROM eclipse-temurin:21-jre

WORKDIR /app

COPY applications/app-service/build/libs/FranchiseManagmentApi.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]