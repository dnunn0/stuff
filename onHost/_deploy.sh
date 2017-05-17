#!/bin/bash
logfile=/c/Logs/firefly.log

PS4='$LINENO: '
set -x

cmd //c "robocopy /J \\\\tsclient\C\WithAws %TEMP%"
version=`cat /tmp/version.txt`

appOrigin=firefly-$version

rm -r /tmp/fireflyStaging/$appOrigin
unzip /tmp/$appOrigin -d /tmp/fireflyStaging
app=$appOrigin
if [ -d c:/firefly/$appOrigin ]; then
  app=$appOrigin-$(date +%s%N)
fi
mv /tmp/fireflyStaging/firefly-$version /c/firefly/$app

port=`cat /c/firefly/LastPort.txt`
port=$((port+1))
echo "$port">/c/firefly/LastPort.txt

#echo -->/dev/null
#echo ----------------- Kill Old Version ----------------- > /dev/null
#date
##echo ---------------------------------------------------->/dev/null
#echo -->/dev/null
#cmd //c "taskkill /f /im java*"
# doesn't kill java process kill $(ps -e | grep "firefly/run.bat"| awk '{print $1}')
#sleep 1s

spec=kalidasa
if [ -f /tmp/fireflyNavDeck.txt ]; then
    spec=join
fi

echo ==>/dev/null
echo ===================== New Version ========================>/dev/null
date
echo ==========================================================>/dev/null
echo ==>/dev/null
echo "/c/firefly/$app/run.bat $port $spec>>$logfile 2>&1" > /c/firefly/run$app.sh
exec /c/firefly/run$app.sh &

#new app should be up. redirect requests to it.
sleep 2s
# have to do netsh int ipv4 install & reboot, also the service IP Helper must be running
#  netsh interface portproxy show all
#  netsh interface portproxy reset
netsh interface portproxy set v4tov4 24680 127.0.0.1 $port