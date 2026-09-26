#!/bin/bash

# List of supported languages, 1 - as default.
LANGUAGES=(
  [1]=en
  [2]=ru
)

PATH=$PATH:/bin:/sbin:/usr/bin:/usr/sbin:/root
PATH=$PATH:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin

if [[ "$OSTYPE" == 'darwin'* ]]; then
 IS_MACOS=$?
fi

ROOT=$(pwd)

if [[ "$IS_MACOS" ]]; then
  USER=$(stat -f '%u' ./)
  GROUP=$(stat -f '%g' ./)
else
  USER=$(stat -c '%u' ./)
  GROUP=$(stat -c '%g' ./)
fi

function lintOpenapi() {
  docker run \
    -it \
    --volume "$ROOT":/data \
    --user "$USER:$GROUP" \
    --entrypoint "/data/docker/openapi/entrypoint.sh" \
    gitlab.icerockdev.com:4567/docker/jamescooke-openapi-validator:0.21.0 "$*"
}

function mergeOpenapi() {
  docker run \
    -it \
    --volume "$ROOT":/app \
    --user "$USER:$GROUP" \
    gitlab.icerockdev.com:4567/docker/node/node-18.16-openapi-merge:latest --config /app/docs/openapi/openapi-merge-settings.json
}

function replace() {
  if [[ "$IS_MACOS" ]]; then
    find docker/ docs/ \
      -type f \
      -exec sed -i '' -e "s|$1|$2|g" {} \;
    sed -i '' -e "s|$1|$2|g" ./README.md
    sed -i '' -e "s|$1|$2|g" ./docker-compose.yaml
    sed -i '' -e "s|$1|$2|g" ./settings.gradle.kts
    sed -i '' -e "s|$1|$2|g" ./.env.example
    sed -i '' -e "s|$1|$2|g" application/src/main/resources/db/changelog/changes/db.changelog-1685440628-insert_superadmin_to_users_table.yaml
    sed -i '' -e "s|$1|$2|g" docs/openapi/openapi.yaml
    sed -i '' -e "s|$1|$2|g" modules/notification/openapi.yaml
    sed -i '' -e "s|$1|$2|g" modules/file/openapi.yaml
    sed -i '' -e "s|$1|$2|g" application/src/main/resources/logback.xml
    sed -i '' -e "s|$1|$2|g" docs/http/rest-client.env.json.example
    sed -i '' -e "/replace/!s|$1|$2|g" ./project.sh
  else
    find docker/ docs/ \
      -type f \
      -exec sed -i "s|$1|$2|g" {} \;
    sed -i "s|$1|$2|g" ./README.md
    sed -i "s|$1|$2|g" ./docker-compose.yaml
    sed -i "s|$1|$2|g" ./settings.gradle.kts
    sed -i "s|$1|$2|g" ./.env.example
    sed -i "s|$1|$2|g" application/src/main/resources/db/changelog/changes/db.changelog-1685440628-insert_superadmin_to_users_table.yaml
    sed -i "s|$1|$2|g" docs/openapi/openapi.yaml
    sed -i "s|$1|$2|g" modules/notification/openapi.yaml
    sed -i "s|$1|$2|g" modules/file/openapi.yaml
    sed -i "s|$1|$2|g" application/src/main/resources/logback.xml
    sed -i "s|$1|$2|g" docs/http/rest-client.env.json.example
    sed -i "/replace/!s|$1|$2|g" ./project.sh
  fi
}

function initProject() {
  if git diff-index --quiet HEAD --; then
    read -r -p 'Enter project name (e.g. My Awesome Project): ' PROJECT_NAME_H
    read -r -p 'Enter project IP address (e.g. 127.0.0.1): ' PROJECT_IP
    read -r -p 'Enter subdomain for dev (api.<subdomain>.icerockdev.com): ' DEV_SUBDOMAIN
    read -r -p 'Enter domain for prod (api.<domain>): ' PROD_DOMAIN
    read -r -p 'Enter admin email (e.g. admin@domain.com): ' ADMIN_EMAIL
    read -r -p 'Enter admin phone (e.g. 81234567890): ' ADMIN_PHONE
    read -r -p 'Enter admin password: ' ADMIN_PASSWORD
    read -r -p 'Enter localization sheet ID: ' SHEET_ID

    PROJECT_NAME=$(tr '[:upper:]' '[:lower:]' <<<"$PROJECT_NAME_H")
    PROJECT_NAME=${PROJECT_NAME// /-}
    ADMIN_PASSWORD_HASH=$(htpasswd -nbBC 10 $ADMIN_EMAIL $ADMIN_PASSWORD | sed 's/.*://')

    replace "#{PROJECT_NAME_H}" "$PROJECT_NAME_H"
    replace "#{PROJECT_NAME}" "$PROJECT_NAME"
    replace "GRADLE_PROJECT_NAME" "$PROJECT_NAME"
    replace "#{PROJECT_IP}" "$PROJECT_IP"
    replace "#{DEV_SUBDOMAIN}" "$DEV_SUBDOMAIN"
    replace "#{PROD_DOMAIN}" "$PROD_DOMAIN"
    replace "#{ADMIN_EMAIL}" "$ADMIN_EMAIL"
    replace "#{ADMIN_PHONE}" "$ADMIN_PHONE"
    replace "#{ADMIN_PASSWORD_HASH}" "$ADMIN_PASSWORD_HASH"
    replace "#{SHEET_ID}" "$SHEET_ID"

    echo "Init messages by locales"
    #initLinter

    echo "Copy local environment files"
    cp .env.example .env
    cp docs/http/rest-client.env.json.example docs/http/rest-client.env.json

    echo 'Congratulations! Your project initialized.'
    echo 'Rollback changes if you want start again.'
  else
    echo 'D-oh! Commit all changes before initialize.'
  fi
}

function cmdLocalize() {
    if [[ ! -d "./sheets-localizations-generator" ]]
    then
        git clone https://gitlab.icerockdev.com/scl/sheets-localizations-generator.git
        cd sheets-localizations-generator || exit 1
    else
        cd sheets-localizations-generator || exit 1
        git pull
    fi

    npm install
    npm start spring strings "#{SHEET_ID}" 'backend!A1:C' ../
}

function echoYell() {
    echo -e "\033[0;33m$1\033[0m"
}

function echoBlue() {
    echo -e "\033[0;34m$1\033[0m"
}

function ShowHelp {
    echoYell 'Help'
    echoBlue 'Usage:'
    echoYell '     ./project.sh COMMAND [keys]'
    echoYell ''
    echoBlue 'Commands:'
    echoYell '  build                                           Run ./gradlew build'
    echoYell '  lint                                            Validate openapi'
    echoYell '  mergeOpenapi                                    Merge openapi by settings docs/openapi/openapi-merge-settings.json'
    echoYell '  initProject                                     Command for init project from boilerplate'
    echoYell '  localize                                        Command for generate localizations'
}


function run() {
  COMMAND=$1

  case "$COMMAND" in
  lint)
    echo "Run $COMMAND project"
    OPENAPI_ARGS=${*:2}
    lintOpenapi "$OPENAPI_ARGS"
    ;;
  mergeOpenapi)
    echo "Run $COMMAND project"
    mergeOpenapi
    ;;
  build)
    echo "Run $COMMAND project"
    ./gradlew build
    ;;
  initProject)
    echo "Run $COMMAND project"
    initProject
    ;;
  localize)
    echo "Run $COMMAND project"
    cmdLocalize
    ;;
  help)
    ShowHelp
    ;;
  *)
    ShowHelp
    ;;
  esac
}

run "$@"
