# Gatling Performance Script:

## Summary

Included here is the Gatling Scripts to test following endpoints:
#### createUser
```
curl --location --request POST 'http://localhost:8080/user/v1/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Mickey",
    "lastName":"Mouse",
    "email":"mickey@gmail.com"
}'
```

#### getUserById
```
curl --location --request GET 'http://localhost:8080/user/v1/find/<valid-uuid>' \
--data-raw ''
```

### System Pre-requisites

* Install Maven latest version.
* Include the following dependency in pom file to generate fake users.
  ```
  <dependency>
  <groupId>com.github.javafaker</groupId>
  <artifactId>javafaker</artifactId>
  <version>1.0.2</version>
  </dependency>
  ```

### Run simulations
#### Run following maven command to run simulation with default values. 
```
mvn gatling:test -Dgatling.simulationClass=perfTestSimulation
```
#### Run following maven command to run simulation with parameter.
```
mvn gatling:test -Dgatling.simulationClass=perfTestSimulation -Durl=http://localhost:8080 -DnUsers=10 -DrampUpTime=5 -DnDuration=10 -DnoOfUsersCreation=100

```
Following is the details of parameters :
```
-Dgatling.simulationClass=perfTestSimulation  // For Simulation class
-Durl=http://localhost:8080  // Base URL
-DnUsers=10 // concurrent user
-DrampUpTime=5 // Ramp-up time in minutes.
-DnDuration=10 // Steady state time in minutes.
-DnoOfUsersCreation=100 // Number of users create for data seeding.
```
###Execution Logs - logback.xml file
Update logback.xml with following values for :
* To turn off logs use 
```
level="OFF"
```
* To enable INFO mode use
```
level="INFO"
```
* To enable TRACE mode use
```
level="INFO"
```
* To enable DEBUG mode use
```
level="DEBUG"
```
