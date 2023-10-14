ENV=$1
doppler setup -p skc-api -c $ENV --no-interactive

mkdir -p certs
doppler secrets get --plain SSL_PRIVATE_KEY > certs/private.key
doppler secrets get --plain SSL_CA_BUNDLE_CRT > certs/ca_bundle.crt
doppler secrets get --plain SSL_CERTIFICATE_CRT > certs/certificate.crt

PK_PASSWORD=$(doppler secrets get --plain PK_PASSWORD)
SSL_KEYSTORE_PASSWORD=$(doppler secrets get --plain SSL_KEYSTORE_PASSWORD)
export PK_PASSWORD
export SSL_KEYSTORE_PASSWORD

# Create keystore w/ certs
cd certs || exit
openssl pkcs12 -export -name skcapi -in certificate.crt -inkey private.key -out skc-api.p12 -password "pass:$PK_PASSWORD"
keytool -importkeystore -deststorepass "$SSL_KEYSTORE_PASSWORD" -destkeystore skc-api.jks \
 -srckeystore skc-api.p12 -srcstoretype PKCS12 -srcstorepass "$PK_PASSWORD"
keytool -import -alias skc-api -trustcacerts -file ca_bundle.crt -keystore skc-api.jks -storepass "$SSL_KEYSTORE_PASSWORD"

cd ..
mv certs/skc-api.jks src/main/resources/skc-api.jks