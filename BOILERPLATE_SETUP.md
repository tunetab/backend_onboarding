### Kotlin spring boilerplate

## Requirements

- JAVA 20
- Docker, Docker-compose

## Init new project

Run for replace variables in project

```shell
./project.sh initProject
```

### Localization

Localization file [template](https://docs.google.com/spreadsheets/d/1wZFDPWaSUWQAFr8Oax9fq2f6lBISNgAVjugJaiwj3sY) for new project

Copy this template to project Production folder, add document ID in `initProject`

Run localization script:
```shell
./project.sh localize
```

Localization string generator works **only** on Node version 20 or below (20.6.0)

Change .properties files encoding to UTF-8 in IDE settings (IntelliJ IDEA example - Settings -> File Encodings -> Properties Files)

Copy  `.env.example` to `.env` and change variables if needed

### CORS setup

By default, cors use API_BASE_URL and ADMIN_BASE_URL from env.

CORS properties in env file:
```properties
CORS_ALLOWED_METHODS=GET,POST,PATCH,PUT,DELETE,OPTIONS,HEAD
CORS_MAX_AGE=3600
CORS_ALLOWED_HEADERS=*
CORS_EXPOSED_HEADERS=*
CORS_ALLOWED_ORIGINS=*
```

For OsX users run command

```shell
ifconfig lo0 alias #{PROJECT_IP}
```

Run developer env (postgres + nginx)

```shell
docker-compose up -d
```

# DateTime reminder

In Spring Boot ^3.0.0 we use standard java.time.LocalDateTime for database timestamps instead of joda-time 

# Local application launch

Add environment variables from .env to Application program arguments

Or use Intellij Idea plugin EnvFile to use variables from file
