FROM openjdk:11
ADD target/sale-taxes.jar /app/sale-taxes.jar
ENTRYPOINT ["java", "-jar", "/app/sale-taxes.jar"]
