# DRM Taxi - Taxi Management System 🚖

A Spring Boot-based backend system for managing taxi operations, user authentication, and administrative functions.

## 🛠 Tech Stack

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot) (3.5)  
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)](https://www.postgresql.org/) (17.4)  
[![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)  
[![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/) (21)

## ⚙️ Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 17.4+
- IDE (IntelliJ/Eclipse recommended)

## 🚀 Getting Started

### 1. Clone Repository

```bash
git clone https://github.com/Rawad-AG/drmtaxi.git
cd drmtaxi
```

### 2. in /src/main/resources you will find the file application.yml.template.txt remove the .template.txt from the name and fill the properties in the yml file

### 3. create the PostgreSQL database with the same name you put in the yml file

### 4. Build with Maven

```bash
mvn clean package
```

### 5. Run application

```bash
java -jar target/drmtaxi-0.0.1-SNAPSHOT.jar
```
