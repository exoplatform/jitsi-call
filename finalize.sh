#!/bin/bash

RECORDINGS_DIR=$1
# Put this script to .jitsi-meet-cfg/jibri (config folder) as finalize.sh
# Need to setup $CALL_APP_URL as ENV variable before running

#Build JWT 
# TODO: store JWT in env variable
secret='mQzPudDBpSAqUwM0FY2r86gNAd6be5tN1xqwdFDOb4Us1DT4Tx'
currentTime=$(date +%s)
# expires in 3 minutes
expires=$(($currentTime + 18000));
# Construct the header
jwt_header=$(echo -n '{"alg":"HS256","typ":"JWT"}' | base64 | sed s/\+/-/g | sed 's/\//_/g' | sed -E s/=+$//)
# ans: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
payloadJson="{\"action\":\"external_auth\", \"exp\": ${expires}}"
# Construct the payload
payload=$(echo -n $payloadJson | base64 | sed s/\+/-/g |sed 's/\//_/g' |  sed -E s/=+$//)
# Convert secret to hex (not base64)
hexsecret=$(echo -n "$secret" | xxd -p | paste -sd "")
# Calculate hmac signature -- note option to pass in the key as hex bytes
hmac_signature=$(echo -n "${jwt_header}.${payload}" |  openssl dgst -sha256 -mac HMAC -macopt hexkey:$hexsecret -binary | base64  | sed s/\+/-/g | sed 's/\//_/g' | sed -E s/=+$//)
# Create the full token
jwt="${jwt_header}.${payload}.${hmac_signature}"


files=( $RECORDINGS_DIR/*.mp4 )
recording=$(basename ${files[0]})
callId=${recording%_*}
#Push the recording to Jitsi Call App
# --write-out '%{http_code}' --silent --output /dev/null
link=$(curl -o --write-out ' %{http_code}' --silent -k -X  GET -H "X-Exoplatform-External-Auth: ${jwt}" $CALL_APP_URL/api/v1/calls/${callId}/uploadLink)
timestamp=$(date +%F_%T)
if [ -n "$link" ]; then
	echo $link
	resp=$(curl -i --write-out '%{http_code}' --silent --output /dev/null -k -X  POST -H "Content-Type: multipart/form-data" -H "X-Exoplatform-Auth: ${jwt}" -F "file=@${files[0]}" $link)
	if [ $resp == 200 ]; then
		echo "[$timestamp] Uploaded recording ${files[0]} to eXo Platform" >> finalize.log
		#rm -rf $RECORDINGS_DIR
		#echo "[$timestamp] Removed folder $RECORDINGS_DIR" >> finalize.log
	else 
		echo "[$timestamp] Couldn't upload recording ${files[0]} to eXo Platform. HTTP_CODE: $resp" >> finalize.log
	fi
else 
	echo "[$timestamp] Couldn't get upload link for recording ${files[0]}" >> finalize.log
fi
exit 0

