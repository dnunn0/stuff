cmd //c "robocopy /J \\\\tsclient\C\WithAws %TEMP%"
cmd //c "taskkill /f /im java*"
sleep 1s
rm -r /c/firefly
rm /c/logs/*
unzip /tmp/firefly -d c:/
/c/firefly/run.bat 24680 KALIDASA >/c/logs/firefly.log 2>&1
