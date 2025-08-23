USER="ec2-user"
DIR_ON_SERVER="api/skc-api"
INSTANCES=1
COMPOSE_FILE="docker-compose.yml"

while [[ $# -gt 0 ]]; do
  case "$1" in
    -d|--dev)
      DIR_ON_SERVER="api/skc-api-dev"
      INSTANCES=1
      COMPOSE_FILE="docker-compose-dev.yml"
      shift
      ;;
    *)
      SERVER="$1"
      shift
      ;;
  esac
done

if [ -z "$SERVER" ]; then
  echo "Error: Server name is required."
  exit 1
fi

echo "Using server $SERVER and directory $DIR_ON_SERVER to deploy API using $COMPOSE_FILE to scale to $INSTANCES instances"
echo "Uploading API files"

# upload appropriate compose file and rename it to common name - docker-compose.yml
rsync --rsync-path="mkdir -p ${DIR_ON_SERVER} && rsync" -avz -e "ssh -i ~/.ssh/skc-server.pem" "$COMPOSE_FILE" "${USER}@${SERVER}:${DIR_ON_SERVER}/docker-compose.yml"
rsync -avz -e "ssh -i ~/.ssh/skc-server.pem" -R "build/libs/skc-api.jar" "${USER}@${SERVER}:${DIR_ON_SERVER}/"

echo "Restaging API"
ssh -i ~/.ssh/skc-server.pem "${USER}@${SERVER}" << EOF
	cd "$DIR_ON_SERVER"
	docker-compose kill
	docker-compose rm -f
	docker-compose pull
	docker-compose up --scale skc-api=$INSTANCES --force-recreate -d
EOF

bash aws-cert-update.sh