ARG JAVA_BUILD_IMAGE

FROM $JAVA_BUILD_IMAGE

WORKDIR /app

COPY docker/run/entrypoint.sh /app/entrypoint.sh
COPY app-build/build/app.jar /app/app.jar

ENTRYPOINT ["/app/entrypoint.sh"]
