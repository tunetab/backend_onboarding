ARG NGINX_BUILD_IMAGE

FROM $NGINX_BUILD_IMAGE

COPY ./docker/common/nginx.conf.tmpl /etc/nginx/templates/default.conf.tmpl

# TODO: Uncomment if added frontend
#COPY ./frontend/admin/build /app

COPY ./docs/openapi/openapi.yaml /app/openapi/openapi.yaml

VOLUME ["/etc/letsencrypt"]
