# Sales Taxes code challenge

This project is intended as a challenge to prove technical knowledge.

It has been developed using Java 11 features. You need OpenJDK 11 in order to compile and execute it.

As an extra feature, this project has been dockerized. If you want to run the project from docker run the following commands:

```
mvn package fabric8:build && docker run -i sales-taxes:1.0 < example.txt
mvn package fabric8:build && docker run -i sales-taxes:1.0 < example2.txt
mvn package fabric8:build && docker run -i sales-taxes:1.0 < example3.txt
```

NOTE: It is important to include docker -i option in order to read from stdin.