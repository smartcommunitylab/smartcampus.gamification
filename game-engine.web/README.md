## Getting Started

In order to start web engine, it is required to run the following command from project root(profile specific). 

### Basic Auth
In order to start the web engine with Basic Auth mode, run the following command. 

```shell
java -jar game-engine.web.jar  --spring.profiles.active=sec  --server.servlet.context-path=/gamification --server.port=6060 --mongo.dbname=gamification-climb-prod-marzo
```

### OAuth
In order to start the web engine with OAuth mode, run the following command. 

```shell
java -jar game-engine.web.jar --spring.profiles.active=platform --server.servlet.context-path=/gamification --server.port=6060 --mongo.dbname=gamification-climb-prod-marzo
```