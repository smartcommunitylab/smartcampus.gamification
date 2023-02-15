# syntax=docker/dockerfile:experimental
FROM node:14 AS react-build
ARG  REACT_APP_API_ENDPOINT=http://localhost:8010/gamification
WORKDIR /app
COPY public /app/public
COPY src /app/src
COPY .env /app/
COPY .yarnrc /app/
COPY package.json ./
COPY tsconfig.json ./
COPY .yarnrc /app/
COPY yarn.lock /app/yarn.lock
RUN --mount=type=cache,target=/app/node_modules/,source=/node_modules/,from=smartcommunitylab/gamification-engine-frontend:cache yarn
RUN --mount=type=cache,target=/app/node_modules/,source=/node_modules/,from=smartcommunitylab/gamification-engine-frontend:cache yarn build

FROM nginx:alpine
ARG USER=gamification
ARG USER_ID=1005
ARG USER_GROUP=gamification
ARG USER_GROUP_ID=1005
ARG USER_HOME=/home/${USER}
ENV APP=game-engine.web
ENV VER=${VER}
RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;
COPY frontend.conf /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/
COPY --from=react-build /app/build /home/${USER}/html
EXPOSE 8080
WORKDIR ${USER_HOME}
USER gamification
CMD ["nginx", "-g", "daemon off;"]