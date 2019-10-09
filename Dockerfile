# syntax=docker/dockerfile:experimental
FROM maven:3-jdk-8-alpine as build
WORKDIR /app
COPY ./game-engine.core /app/game-engine.core
COPY ./game-engine.web /app/game-engine.web
# install nodejs
RUN apk --update add nodejs npm
# install git client
RUN apk --update add git
# install bower
RUN npm install -g bower
WORKDIR game-engine.core
RUN --mount=type=cache,target=/root/.m2 mvn -Dmaven.test.skip=true install;
WORKDIR ../game-engine.web
RUN mkdir logs
RUN if [ -e "/app/run-configs/users.yml" ]; then cp /app/run-configs/users.yml /app/game-engine.web/src/main/resources/users.yml; fi;
RUN cd /app/game-engine.web/src/main/resources/consoleweb-assets && bower --allow-root install
RUN --mount=type=cache,target=/root/.m2 cd /app/game-engine.web && mvn -Dmaven.test.skip=true package

FROM openjdk:8-alpine
ENV FOLDER=/app/game-engine.web/target
ARG VER=0.1
ENV APP=game-engine.web.jar
ARG USER=gamification
ARG USER_ID=3003
ARG USER_GROUP=gamification
ARG USER_GROUP_ID=3003
ARG USER_HOME=/home/${USER}

RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;
RUN apk add --no-cache tzdata

WORKDIR  /home/${USER}/app
RUN chown ${USER}:${USER_GROUP} /home/${USER}/app
COPY --from=build --chown=gamification:gamification ${FOLDER}/${APP} /home/${USER}/app
EXPOSE 8010

USER gamification
CMD ["sh","-c","java -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.port=7777 -Dcom.sun.management.jmxremote.rmi.port=7777 -Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=false -DlogFolder=${LOG_PATH} -jar game-engine.web.jar --spring.profiles.active=${SPRING_PROFILE}"]
