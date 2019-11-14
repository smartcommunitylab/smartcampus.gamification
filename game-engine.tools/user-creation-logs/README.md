## Quickstart

### Build

Run `mvn clean package`

### Run

Create a registrations.csv for game `GAME_ID` from play&go database
```
mongoexport --db playandgo -c player -f playerId,personalData.timestamp --csv -q "{gameId:'GAME_ID'}" -o 'FOLDER/registrations.csv'
```

Run tool

```
java -jar user-creation-logs-0.0.1-SNAPSHOT LOG_FOLDER REGISTRATION_CSV_PATH GAME_ID
```

* LOG_FOLDER: folder where the stats logs are and where tool will add the UserCreation entries
* REGISTRATION_CSV_PATH: path of file `registrations.csv` created by above instructions
* GAME_ID: the id of the game
