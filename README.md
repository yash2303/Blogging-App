# Spring Boot Blogging Application

This application is a simple blogging platform built using Java 17 and Spring Boot. 
It supports user registration and login. It allows logged-in users to create, read, update, and delete blog posts. 
This application serves as the backend for the React-Redux frontend application which is available at https://github.com/yash2303/blogging-app-frontend.git and runs on port 3000.

## Requirements
* Java 17
* Gradle
* Spring Boot

## Getting Started
1. Clone the repository
```shell
git clone https://github.com/yash2303/Blogging-App.git
```
2. Build the project
```shell
./gradlew build
```
3. Run the application
```shell
./gradlew bootRun
```

## API documentation
The application has Swagger integrated, so you can access the API documentation by going to the following URL in your browser:
```
http://localhost:8484/swagger-ui.html
```

## How to Contribute
We welcome contributions to this project! Please follow these guidelines when submitting pull requests:

All code changes should be accompanied by relevant test cases.
* The project should be able to build and run without errors.
* Follow the code style and conventions used in the existing code.

## Contact Information
The project maintainer can be contacted at galavyashasvi23@gmail.com