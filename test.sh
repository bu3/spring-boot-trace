#!/bin/bash

usage() {
  echo "Usage: $0 {tomcat|jetty-default|jetty-custom}"
  echo ""
  echo "Commands:"
  echo "  tomcat         Run tests with Tomcat (should pass)"
  echo "  jetty-default  Run tests with default Jetty (2 tests should fail)"
  echo "  jetty-custom   Run tests with Jetty and custom dispatcher (tests should pass)"
  exit 1
}

testWithTomcat(){
  echo "-----------------------------------------"
  echo "Running tests with Tomcat - it should pass"
  echo "-----------------------------------------"
  cat DemoApplication.java_without_custom_dispatcher_servlet > src/main/java/com/example/demo/DemoApplication.java
  ./mvnw -f pom-tomcat.xml clean test
}

testWithDefaultJetty() {
  echo "-----------------------------------------"
  echo "Running tests with default Jetty - 2 Test should fail"
  echo "-----------------------------------------"
  cat DemoApplication.java_without_custom_dispatcher_servlet > src/main/java/com/example/demo/DemoApplication.java
  ./mvnw clean test
}

testWithJettyWithCustomDispatcher() {
  echo "-----------------------------------------"
  echo "Running tests with default Jetty - 2 Test should fail"
  echo "-----------------------------------------"
  cat DemoApplication.java_custom_dispatcher_servlet > src/main/java/com/example/demo/DemoApplication.java
  ./mvnw clean test
}

testWithTomcat
testWithDefaultJetty
testWithJettyWithCustomDispatcher


# Run all tests if no argument is provided
if [ -z "$1" ]; then
  echo "No parameter provided. Running all tests..."
  testWithTomcat
  testWithDefaultJetty
  testWithJettyWithCustomDispatcher
  exit 0
fi

# Check for input argument and call the appropriate function
case "$1" in
  tomcat)
    testWithTomcat
    ;;
  jetty-default)
    testWithDefaultJetty
    ;;
  jetty-custom)
    testWithJettyWithCustomDispatcher
    ;;
  *)
    usage
    ;;
esac
