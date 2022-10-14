FROM node:16.12.0-stretch as buildstep
RUN mkdir -p /project/
RUN mkdir -p /project/dist/nadin-timer

RUN apt update -y && \
    apt install -y --no-install-recommends --no-install-suggests \
    nginx \
    ca-certificates &&\
    rm -rf /var/cache/apt/ &&\
    rm -rf /var/lib/apt/lists/*

WORKDIR /project
ADD . /project
RUN npm install --no-audi && \
    npm run lint:prod && \
    npm run build:prod && \
    rm -r node_modules


FROM nginx:mainline-alpine
WORKDIR /project
COPY --from=buildstep /project/dist /project
ADD docker/nginx/nginx.conf /etc/nginx/conf.d/default.conf
RUN rm -rf /etc/apt/apt.conf.d/90nginx
EXPOSE 96

STOPSIGNAL SIGTERM

CMD ["nginx", "-g", "daemon off;"]
