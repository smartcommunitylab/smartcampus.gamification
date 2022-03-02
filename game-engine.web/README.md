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

### Setup
In order to setup the database copy the provided db.json file inside the import directory path as specified in engine-core.properties file, the import can be performed invoking the API

```shell
http://localhost:8010/gamification/admin/importJsonDB
```


### Usage
In order to test usage of gamification one can try to assign challenge to test user

```shell
POST http://localhost:8010/gamification/data/game/{gameId}/player/{playerId}/challenges
```
with json body

```shell
{
  "data": {"bonusScore" : 250.0,"periodName" : "weekly", "bonusPointType" : "green leaves","counterName" : "NoCar_Trips","target" : 4.0},
  "modelName":"absoluteIncrement",
  "instanceName":"testInstance",
  "start": 1644447600000,
  "end": 1654812000000
}
```

In order to assign point to user, test execute API call can be made in following way.

```shell
POST http://localhost:8010/gamification/gengine/execute
```
with json body

```shell
{"gameId":"{gameId}","playerId":"{playerId}","actionId":"save_itinerary", "data":{"travelId":"1","walkDistance":0.25}}
```
