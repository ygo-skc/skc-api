if [ $# -eq 0 ]
	then
		echo "Doppler env name needs to be provided"
		exit 1
fi

ENV=$1
doppler setup -p skc-api -c $ENV --no-interactive

cat certs/private.key | doppler secrets set SSL_PRIVATE_KEY
cat certs/ca_bundle.crt | doppler secrets set SSL_CA_BUNDLE_CRT
cat certs/certificate.crt | doppler secrets set SSL_CERTIFICATE_CRT