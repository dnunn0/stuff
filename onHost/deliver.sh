cp  //tsclient/C/WithAws/firefly.zip /tmp
cmd //c "taskkill /f /im java*"
rm -r /c/firefly
rm /c/logs/*
unzip /tmp/firefly -d c:/
