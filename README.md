Gamification Engine is a platform that permits to define and execute score based games.

Platform is developed in Java using Apache Maven as build tool.

## Components
* game-engine.core: it contains Drools engine implementation and models
* game-engine.web: REST API, admin web interface and security layer
* game-engine.test: junit test environment to test a new game definition
* game-engine.games: collection of production games.


## Quickstart run

You can use docker to have a quick running system.

You can find configurations into the `gamification.env` file.

1. run `docker-compose up` from the root folder.

**NOTE**
* gamification-engine will be up and running on host port **8010** at url **http://http://localhost:8010/gamification/consoleweb/**
* mongodb container exposes to host port **5000** to inspect the data inside
* gamification-engine exposes a JMX connection on host port **7777**


## Quickstart build

To build a complete game engine instance you need following tools: 
* bower

Build steps:
1. move to folder `game-engine.web/src/main/resources/consoleweb-assets`
2. run command `bower install`
3. follow instructions from step 2 of [the setup chapter of wiki](https://github.com/smartcommunitylab/smartcampus.gamification/wiki/Setup#build) 

## Upgrades

### v2.1.0 -> v2.2.0

In version 2.2.0 we have changed the persistence model representation of playerState and pointConcept, so if you migrate from v2.1.0 or prior version to v.2.2.0 you have to run following script
to you mongo database.

```
mongo DB_NAME game-engine.core/src/main/scripts/migration-db/2.1.0-to-2.2.0/step1.js
mongo DB_NAME game-engine.core/src/main/scripts/migration-db/2.1.0-to-2.2.0/step2.js
mongo DB_NAME game-engine.core/src/main/scripts/migration-db/2.1.0-to-2.2.0/step3.js
```

## Documentation
Read the project [wiki](https://github.com/smartcommunitylab/smartcampus.gamification/wiki) for all details about the project

## Licence
Project is licensed under the Apache License Version 2.0
