Gamification Engine is a platform that permits to define and execute score based games.

Platform is developed in Java using Apache Maven as build tool.

## Components
* game-engine.core: it contains Drools engine implementation and models
* game-engine.web: REST API, admin web interface and security layer
* game-engine.test: junit test environment to test a new game definition
* game-engine.games: collection of production games.


## Quickstart build

To build a complete game engine instance you need following tools: 
* bower

Build steps:
1. move to folder `game-engine.web/src/main/resources/consoleweb-assets`
2. run command `bower install`
3. follow instructions from step 2 of [the setup chapter of wiki](https://github.com/smartcommunitylab/smartcampus.gamification/wiki/Setup#build) 

## Documentation
Read the project [wiki](https://github.com/smartcommunitylab/smartcampus.gamification/wiki) for all details about the project

## Licence
Project is licensed under the Apache License Version 2.0
