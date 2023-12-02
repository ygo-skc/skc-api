if [ $# -eq 0 ]
	then
		echo "Need server name"
		exit 1
fi

SERVER=$1
USER="ec2-user"

echo "Using server: $SERVER"

rsync -avz -e "ssh -i ~/.ssh/skc-server.pem" docker-compose.yml "${USER}@${SERVER}:skc-api/"
rsync -avz -e "ssh -i ~/.ssh/skc-server.pem" -R "build/libs/skc-api.jar" "${USER}@${SERVER}:skc-api/"