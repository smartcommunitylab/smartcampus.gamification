## Quickstart

### Build

Run `mvn clean package`

### Run

Run `java -jar log2elastic-0.0.1-SNAPSHOT.jar` FOLDER_LOGS [--elastic5]

* FOLDER_LOGS: folder where the logs file are
* --elastic5: by default application use elasticsearch7 syntax to load logs, use this option if you want to load logs on elasticsearch5
