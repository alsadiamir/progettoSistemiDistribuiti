pack-secrets:
	@cd secrets && zip -r secrets.zip ./*
	@mv secrets/secrets.zip ./

install:
	apt install redis-server mysql-server docker -y

setup-secrets:
	unzip -o secrets.zip -d .
	mkdir -p $HOME/canteen
	cp ./Backend/google-services-admin.json $HOME/canteen/google-services-admin.json
	sudo docker load --input aa.canteeui.latest.tar
	@echo Done!

run-frontend: setup-secrets
	cd frontend && yarn start

run-backend: setup-secrets
	. ./Backend/setup-env.sh && cd Backend/canteen && ./gradlew bootRun

run-redis:
	redis-server

run-mysql:
	service mysql start

run-prometheus:
	docker run -d --name prometheus_demo -p 9090:9090 -v `pwd`/Backend/canteen/prometheus/:/etc/prometheus/ prom/prometheus

run-grafana:
	docker run -d -p 5000:3000 grafana/grafana

run-all:
	echo "START"; \
		make setup-secrets & \
	 	make run-redis & \
		make run-mysql & \
        make run-backend & \
        make run-prometheus & \
        make run-grafana & \
		make run-frontend & \
        wait; \
    echo "DONE"
