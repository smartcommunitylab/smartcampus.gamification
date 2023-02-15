# syntax=docker/dockerfile:experimental
FROM maven:3-openjdk-11 as mvn
COPY ./game-engine.core /tmp/game-engine.core
COPY ./game-engine.web /tmp/game-engine.web
WORKDIR /tmp/game-engine.core
RUN --mount=type=cache,target=/root/.m2,source=/.m2,from=smartcommunitylab/gamification-engine:cache mvn install -DskipTests
WORKDIR /tmp/game-engine.web
RUN --mount=type=cache,target=/root/.m2,source=/.m2,from=smartcommunitylab/gamification-engine:cache mvn install -DskipTests

FROM eclipse-temurin:11-alpine
ARG VER=3.0.0
ARG USER=gamification
ARG USER_ID=1005
ARG USER_GROUP=gamification
ARG USER_GROUP_ID=1005
ARG USER_HOME=/home/${USER}
ENV FOLDER=/tmp/target
#dslab.playandgo.engine-1.0.jar
ENV APP=game-engine.web
ENV VER=${VER}
# create a user group and a user
RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;

WORKDIR ${USER_HOME}
COPY --chown=gamification:gamification --from=mvn /tmp/game-engine.web/target/${APP}.jar ${USER_HOME}
USER gamification
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar ${APP}.jar --spring.profiles.active=sec"]
