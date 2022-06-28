# syntax=docker/dockerfile:experimental
FROM maven:3-openjdk-11 as mvn
COPY ./ /tmp/
WORKDIR /tmp/
RUN mvn clean install -DskipTests

FROM adoptopenjdk/openjdk11:alpine
ARG VER=3.0.0
ARG USER=gamification
ARG USER_ID=1005
ARG USER_GROUP=gamification
ARG USER_GROUP_ID=1005
ARG USER_HOME=/home/${USER}
ENV FOLDER=/tmp/target
ENV APP=log2timescaledb-1.0-jar-with-dependencies
ENV VER=${VER}
# create a user group and a user
RUN  addgroup -g ${USER_GROUP_ID} ${USER_GROUP}; \
     adduser -u ${USER_ID} -D -g '' -h ${USER_HOME} -G ${USER_GROUP} ${USER} ;

WORKDIR ${USER_HOME}
COPY --chown=gamification:gamification --from=mvn /tmp/target/${APP}.jar ${USER_HOME}
USER gamification
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar ${APP}.jar ${FOLDER_INPUT} ${CONN_URL}"]
