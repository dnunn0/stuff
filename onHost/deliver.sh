cp  //tsclient/C/WithAws/firefly.zip /tmp
cmd //c "taskkill /f /im java*"
sleep 1s
rm -r /c/firefly
rm /c/logs/*
unzip /tmp/firefly -d c:/
