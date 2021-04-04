# scrum-poker-backend

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.scrumPoker.ScrumPokerApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
cd scrum-poker-backend
mvn spring-boot:run
```

The app will run a socket connection server with endpoint [http://localhost:8090](http://localhost:3000)

# scrum-poker

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

Installs libraries:

```shell
cd scrum-poker
npm i
```

Runs the app in the development mode.\

```shell
npm start
```
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

