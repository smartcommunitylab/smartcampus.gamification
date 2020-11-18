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
