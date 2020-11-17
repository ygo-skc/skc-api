server=$1
user="ec2-user"

if [ $# -eq 0 ]
	then
		echo "Need server name"
fi

ssh -i ~/.ssh/ygo-api.pem "${user}@${server}" << EOF
	cd api
  docker-compose stop
  docker-compose up -d
EOF