#!/bin/bash
logfile=/c/Logs/firefly.log
PS4='$LINENO: '
set -x

cmd //c "robocopy /J \\\\tsclient\C\WithAws %TEMP%"
version=`cat /tmp/version.txt`

app=firefly-$version

rm -r c:/firefly/$app
unzip /tmp/$app -d c:/firefly

echo -->/dev/null
echo ----------------- Kill Old Version ----------------- > /dev/null
date
echo ---------------------------------------------------->/dev/null
echo -->/dev/null

port=`cat /tmp/LastPort.txt`
port=$((port+1))
echo "$port">/tmp/LastPort.txt

cmd //c "taskkill /f /im java*"
# doesn't kill java process kill $(ps -e | grep "firefly/run.bat"| awk '{print $1}')
sleep 1s

echo ==>/dev/null
echo ===================== New Version ========================>/dev/null
date
echo ==========================================================>/dev/null
echo ==>/dev/null
/c/firefly/$app/run.bat $port join>>$logfile 2>&1
