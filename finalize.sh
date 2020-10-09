#!/bin/bash

RECORDINGS_DIR=$1
# Put this script to .jitsi-meet-cfg/jibri (config folder) as finalize.sh
# Need to setup $CALL_APP_URL as ENV variable before running
files=( $RECORDINGS_DIR/*.mp4 )
#Push the recording to Jitsi Call App
resp=$(curl -i --write-out '%{http_code}' --silent --output /dev/null -X  POST -H "Content-Type: multipart/form-data" -F "file=@${files[0]}" $CALL_APP_URL/recordings/)
timestamp=$(date +%F_%T)
if [ $resp == 200 ]; then
	echo "[$timestamp] Uploaded recording ${files[0]} to Jitsi Call APP" >> finalize.log
	rm -rf $RECORDINGS_DIR
	echo "[$timestamp] Removed folder $RECORDINGS_DIR" >> finalize.log
else 
	echo "[$timestamp] Couldn't upload recording ${files[0]} to Jitsi Call APP. HTTP_CODE: $resp" >> finalize.log
fi
exit 0
