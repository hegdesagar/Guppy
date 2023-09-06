Steps to run kafka container
1. run docker compose
	docker-compose -up -d
	
To check the contents of the queue
docker exec -it <kafka_container_name_or_id> /bin/bash
	-> docker exec -it visualiser-web-zookeeper-1 /bin/bash	
kafka-console-consumer --bootstrap-server localhost:9092 --topic <your_topic_name> --from-beginning
	-> kafka-console-consumer --bootstrap-server localhost:9092 --topic BROADCAST-CHANNEL --from-beginning


			
