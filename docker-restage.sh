server=$1
user="ec2-user"

if [ $# -eq 0 ]
	then
		echo "Need server name"
fi

ssh -i ~/.ssh/ygo-api.pem "${user}@${server}" << EOF
	cd api
	docker-compose kill
	docker-compose rm -f
	docker-compose up --scale ygo-api=3 -d
EOF