SERVER=$1
USER="ec2-user"

if [ $# -eq 0 ]
	then
		echo "Need server name"
		exit -1
fi

SERVER=$1
USER="ec2-user"
DIR_ON_SERVER="api/skc-api"

echo "Using server $SERVER and directory $DIR_ON_SERVER to sync prod API"

echo "Uploading API files"
rsync --rsync-path="mkdir -p ${DIR_ON_SERVER} && rsync" -avz -e "ssh -i ~/.ssh/skc-server.pem" docker-compose.yml "${USER}@${SERVER}:${DIR_ON_SERVER}/"
rsync -avz -e "ssh -i ~/.ssh/skc-server.pem" -R "build/libs/skc-api.jar" "${USER}@${SERVER}:${DIR_ON_SERVER}/"