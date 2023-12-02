if [ $# -eq 0 ]
	then
		echo "Need server name"
		exit 1
fi

server=$1
user="ec2-user"

ssh -i ~/.ssh/skc-server.pem "${user}@${server}" << EOF
	cd skc-api
	docker-compose kill
	docker-compose rm -f
	docker-compose pull
	docker-compose up --scale skc-api=2 -d
EOF