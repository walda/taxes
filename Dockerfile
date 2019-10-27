FROM openjdk:11
ADD target/sales-taxes.jar /app/sales-taxes.jar
ENTRYPOINT ["java", "-jar", "/app/sales-taxes.jar"]
