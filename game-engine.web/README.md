## Getting Started

Follow [Setup](https://github.com/smartcommunitylab/smartcampus.gamification/wiki/Setup) wiki page to run the gamification-engine

### Basic Auth
In order to start the web engine with Basic Auth mode, run the following command. 

```shell
java -jar game-engine.web.jar  --spring.profiles.active=sec
```

### OAuth
In order to start the web engine with OAuth mode, run the following command. 

```shell
java -jar game-engine.web.jar --spring.profiles.active=platform
```

### Docker Image
In order to start the image run the following command from root

```
docker-compose up
```

- gamification-engine will be up and running on host port 8010. APIs can be invoked using the url prefix

  _http://localhost:8010/gamification/swagger-ui/index.html#/_

- administration console  is available at url  
  
  _http://localhost:3000/#game_
