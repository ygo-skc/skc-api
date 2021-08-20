server=$1
user="ec2-user"

if [ $# -eq 0 ]
	then
		echo "Need server name"
fi

ssh -i ~/.ssh/skc-server.pem "${user}@${server}" << EOF
	mkdir -p api/build/libs/
EOF

sftp -i ~/.ssh/skc-server.pem "${user}@${server}" << EOF
	cd api
	put docker-compose.yml
	cd build/libs
	lcd build/libs
	put *.jar skc-api.jar
EOF