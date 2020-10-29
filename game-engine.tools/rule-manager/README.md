## Quickstart

### Build

Run `mvn clean package`

### Run

```
java -jar rule-manager-0.1.0-SNAPSHOT.jar \
--url <GAMIFICATION_ENGINE_URL> \
--username <USERNAME> \
--password <PASSWORD> \
[--from <UPDATES_FOLDER>]
```

```
--url: url to gamification engine
--username: valid gamification engine username
--password: valid gamification engine password
--from: (optional) folder path of rules to upload
```
