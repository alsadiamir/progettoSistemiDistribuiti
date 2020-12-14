#!/bin/bash

# insert a user
curl -X POST -H "Content-Type: application/json" -d '{"mail":"exampleATuniboDOTit","device":"192.168.49.1"}' http://localhost:8080/user
curl -X POST -H "Content-Type: application/json" -d '{"mail":"example2ATuniboDOTit","device":"192.168.49.2"}' http://localhost:8080/user
# get a user
curl -X GET http://localhost:8080/user/1



# insert a room SQL for init
# INSERT INTO test.room (id,name,address,opening_time,closing_time) VALUES('1','mensa_ing','via risorgimento 1', '12:00', '14:30');

# insert a seat in room1 for init
# INSERT INTO test.seat (id,x,y,room_id) VALUES('1','1','1','1');
# INSERT INTO test.seat (id,x,y,room_id) VALUES('2','1','2','1');


# insert reservation
curl -X POST -H "Content-Type: application/json" -d '{"user":{"id":1},"seat":{"id":1},"reservationDate":"12/11/2015","firstBlockReserved":1,"blocksReserved":5}' http://localhost:8080/reservation
#update reservation
curl -X POST -H "Content-Type: application/json" -d '{"user":{"id":1},"seat":{"id":1},"reservationDate":"12/11/2015","firstBlockReserved":1,"blocksReserved":4}' http://localhost:8080/reservation/update/ID

#create docker container prometheus
sudo docker run -d --name prometheus_demo -p 9090:9090 -v /home/amir/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
#create docker container grafana
sudo docker run -d -p 3000:3000 grafana/grafana
#create docker 

#stop containers
sudo docker stop <nome_container>

#list of stopped containers
sudo docker ps --filter "status=exited"

#start a stopped container
sudo docker start <codice_container>

#remove a stopped container
sudo docker rm <codice_container>

