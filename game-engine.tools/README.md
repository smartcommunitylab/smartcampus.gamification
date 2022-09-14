## Gamification Engine Tools
A set of tools to manage gamification operations

### log2stats
Extract statistic events from game-engine execution logs

Follow instructions [HERE](log2stats/README.md)
### challenge-db-stats
Create challenge events and log them into statistic format reading the database or gamification engine.
Events are: 
* challenge assignment
* challenge completion
### converter-logs
Convert statistic logs from version 1.0 to 2.0
### user-creation-logs
Create user creation events and log them into statistic format reading the database of player registrations

Follow instructions [HERE](user-creation-logs/README.md)
### log2elastic
Push gamification events to elasticsearch storage. Take as input log statistic format.

### log2timescaledb
Push gamification events to postgres storage with timescaledb extension. The timescabledb Take as input log statistic format. 
TimescaleDB is the open-source relational database for time-series and analytics. It is then integrated with Grafana console for
for complex analytics data operation for the realization of gamification statistics dashboard.

Follow instructions [HERE](log2elastic/README.md)
### dashboards/kibana
Export of sample kibana dashboard for gamification statistic
### clone-game
Utility to clone a game in the same or different database and server
### rule-manager
A cli tool to manager the rules of a game
Follow instructions [HERE](rule-manager/README.md)
