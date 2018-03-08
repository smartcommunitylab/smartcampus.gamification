FROM maven:3.5.2-jdk-8-alpine

WORKDIR /app

ADD . /app

EXPOSE 8010
CMD cd game-engine.core; mvn -Dmaven.test.skip=true install; cd ../game-engine.web; if [ ! -d "logs" ]; then mkdir logs; fi; mvn -Dmaven.test.skip=true package; java -Xms512m -Xmx1024m -XX:MaxMetaspaceSize=1024m -DlogFolder=/app/game-engine.web/logs -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.port=7777 -Dcom.sun.management.jmxremote.rmi.port=7777  -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=false -jar target/game-engine.web.jar --spring.profiles.active=sec --server.contextPath=/gamification --task.persistence.activate=false --mongo.host=mongodb --mongo.dbname=gamification --server.port=8010
