#!/bin/bash
logfile=/c/Logs/firefly.log
PS4='$LINENO: '
set -x

cmd //c "robocopy /J \\\\tsclient\C\WithAws %TEMP%"
unzip /tmp/firefly -d c:/firefly-wait

echo -->/dev/null
echo ----------------- Kill Old Version ----------------- > /dev/null
date
echo ---------------------------------------------------->/dev/null
echo -->/dev/null

cmd //c "taskkill /f /im java*"
# doesn't kill java process kill $(ps -e | grep "firefly/run.bat"| awk '{print $1}')
sleep 1s

rm -r /c/firefly
mv /c/firefly-wait/firefly /c/firefly
rm -r /c/firefly-wait
echo ==>/dev/null
echo ===================== New Version ========================>/dev/null
date
echo ==========================================================>/dev/null
echo ==>/dev/null
/c/firefly/run.bat 24680 KALIDASA >>$logfile 2>&1
