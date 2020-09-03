## Quickstart

### Build

Run `mvn clean package`

### Run

Run `java -jar log2elastic-0.0.1-SNAPSHOT.jar` FOLDER_LOGS [--elastic5]

* FOLDER_LOGS: folder where the logs file are
* --elastic5: by default application use elasticsearch7 syntax to load logs, use this option if you want to load logs on elasticsearch5

## Configuration

At the moment some configurations are "code" side.

Set in `Application.java` 
* GAMEID: gameId of the event to insert into ElasticSearch
* PREFIX_PROCESSED_FILE: a prefix for `gamification.stats.log` files

```
 private static final String PREFIX_PROCESSED_FILE = "NEW-";
 private static final String GAMEID = "";
```

Set in `ESHelper.java`
* ELASTIC_URL: endpoint of elastic server
* INDEX_PREFIX: prefix for index name

```
 private static final String ELASTIC_URL = "http://localhost:9200";
 private static final String INDEX_PREFIX = "gamification-stats-";
```


