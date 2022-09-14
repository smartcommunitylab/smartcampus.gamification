## Quickstart

### Build

Run `mvn clean package`

### Run

Run `java -jar target\log2timescaledb-1.0-jar-with-dependencies.jar` FOLDER_LOGS CONNECTION_URL

* FOLDER_LOGS: folder where the logs file are
* FOLDER_STATS: postgres schema connection url 

For example

```shell
 java -jar target\log2timescaledb-1.0-jar-with-dependencies.jar "C:\home\dev\gamification\logs" "jdbc:postgresql://localhost:5432/gamification?user=postgres&password=root"
```

For more info about timescaledb.

```
https://github.com/timescale/timescaledb
```




