cmd //c "taskkill /f /im java*"
cp  //tsclient/C/WithAws/firefly.zip /tmp
rm -r /c/firefly
rm /c/logs/*
unzip /tmp/firefly -d c:/
/c/firefly/run.bat 24680 KALIDASA >/c/logs/firefly.log 2>&1
