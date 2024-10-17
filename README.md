## SB3 - HTTP TRACE method

### Tomcat 
- returns 405 on the server port 
- returns 405 on the actuator port

### Jetty 
- returns 200 on the server port 
- returns 200 on the actuator port

### Jetty with a Custom DispatcherServlet
- returns HTTP status code defined in the [Custom DispatcherServlet](src/main/java/com/example/demo/CustomDispatcherServlet.java) on the server port 
- returns 200 on the actuator port



## Test

#### Tomcat - 2 Tests pass
```
./test.sh tomcat
```

#### Jetty - default - 2 Tests fail
```
./test.sh jetty-default
```

#### Jetty - Custom DispatcherServlet - 1 Test fails / 1 Pass
```
./test.sh jetty-custom
```