## SB3 - HTTP TRACE method

This is a demo project I made for this [issue](https://github.com/spring-projects/spring-boot/issues/42724#issuecomment-2428745504) I opened in the Spring Boot repository.
Same thing is [here](https://stackoverflow.com/questions/79100902/spring-boot-3-jetty-mgmt-server-allows-trace-method-calls) on Stackoverflow

This projects shows how to disable `TRACE` method with Spring Boot 3 on Jetty 12 for both the regular application and the Spring actuator server.



## Test
```
mvn clean test
```