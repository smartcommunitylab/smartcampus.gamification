#!/bin/sh

curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"43","data":{"bikesharing": true}}
EOF

curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"14","data":{"walkDistance": 10.0}}
EOF

curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"5","data":{"p+r": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"67","data":{"p+r": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"177","data":{"p+r": true}}
EOF

curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"79","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"168","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"169","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"171","data":{"walkDistance": 1.0,"bikeDistance": 4.0, "sustainable": true}}
EOF

curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"121","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"157","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"158","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF
curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"165","data":{"walkDistance": 5.0, "busDistance": 13.0, "bikesharing": true}}
EOF

curl http://localhost:8900/execute -H "Content-type: application/json" -d @- << EOF 
{"actionId":"save_itinerary","userId":"167","data":{"walkDistance": 3.0, "trainDistance": 25.0}}
EOF
