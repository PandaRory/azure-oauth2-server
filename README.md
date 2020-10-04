# azure-oauth2-server
This sample project follows Microsoft [tutorial](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory) to demonstrate Azure Active Directory OAuth 2.0 with Spring Boot.

### Requirement
- Application Properties:
  - Azure Active Directory tenant-id
  - Azure Active Directory user-group.allowed-groups
  - Azure client-id
  - Azure client-secret

### Usage
1. Clone the repository, or download the repository and extract the .zip file.
```sh
git clone https://github.com/PandaRory/azure-oauth2-server.git
```
2. Specify the settings in `application.properties` file
3. Build your Spring Boot application with Maven and run it
```sh
mvn clean package
mvn spring-boot:run
```
4. Open http://localhost:8080 in a web browser.
5. Visit http://localhost:8080/logout to relogin.
