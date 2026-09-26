### Back_Onboarding

## Requirements

- JAVA 20
- Docker, Docker-compose

## Setup

Copy  `.env.example` to `.env` and change variables if needed

For OsX users run command

```shell
ifconfig lo0 alias 127.0.0.1
```

Run developer env (postgres + nginx)

```shell
docker compose up -d
```

## Database migrations

Apply migrations
```shell
./gradlew liquibaseUpdate
```

Rollback 2 changesets
```shell
./gradlew liquibaseRollbackCount -PliquibaseCommandValue=2
```

Create new migration file
```shell
./gradlew liquibaseNewChangeLog -PliquibaseCommandValue=create_users_table
```
## Swagger UI on server
Swagger UI relative URL `/swagger-ui/index.html`, 
it is required basic HTTP Auth with credentials provided in environment:
```env
HTTP_AUTH_USER
HTTP_AUTH_PASSWORD
```

## CI/CD
Deployment process described in the [.gitlab-ci.yml](.gitlab-ci.yml) file.

### CI server setup

```
-- Convert id_rsa key to oneline string (for the SSH_DEPLOY_KEY CI-var in {env}_deploy_env file. See below)
cat ~/.ssh/id_rsa | tr '\n' _ | base64 -w 0
```

### CI/CD environment variables
Environment variables used in CI/CD process. Environment variables will be applied to environments via the runner.
The above list of variables should be listed in Gitlab:
```text

-- dev_deploy_env and prod_deploy_env 
REMOTE_HOST
REMOTE_PORT
REMOTE_USER
REMOTE_DIR
SSH_DEPLOY_KEY

-- dev_app_env and prod_app_env
DATABASE_PASS
DATABASE_ROOT
JWT_SECRET
S3_ACCESS_KEY_ID
S3_SECRET_ACCESS_KEY
HTTP_AUTH_USER
HTTP_AUTH_PASSWORD

```
