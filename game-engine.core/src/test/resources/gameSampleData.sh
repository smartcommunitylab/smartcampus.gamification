#!/bin/sh
#
#    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

GAMEID=5537a29744ae60a9aad97329 

curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID", "actionId":"save_itinerary","userId":"43","data":{"bikesharing": true}}
EOF

curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"14","data":{"walkDistance": 10.0}}
EOF

curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"5","data":{"p+r": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"67","data":{"p+r": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"177","data":{"p+r": true}}
EOF

curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"79","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"168","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"169","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"171","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF

curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"121","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"157","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"158","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF
curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"165","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF

curl http://localhost:8080/gamification/gengine/execute -H "Content-type: application/json" -d @- << EOF 
{"gameId": "$GAMEID","actionId":"save_itinerary","userId":"167","data":{"walkDistance": 3.0, "trainDistance": 25.0}}
EOF
