ARN=$(doppler secrets get --plain SSL_CERT_AWS_ARN)

aws acm import-certificate --certificate-arn "$ARN" \
  --certificate "fileb://certs/certificate.crt" --certificate-chain "fileb://certs/ca_bundle.crt" --private-key "fileb://certs/private.key"