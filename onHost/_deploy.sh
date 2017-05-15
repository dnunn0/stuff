#!/bin/bash
logfile=/c/Logs/firefly.log
PS4='$LINENO: '
set -x

cmd //c "robocopy /J \\\\tsclient\C\WithAws %TEMP%"
unzip /tmp/firefly -d c:/firefly-wait


# ----------------- Kill Old Version -----------------
date
# ----------------------------------------------------

cmd //c "taskkill /f /im java*"
# doesn't kill java process kill $(ps -e | grep "firefly/run.bat"| awk '{print $1}')
sleep 1s

rm -r /c/firefly
mv /c/firefly-wait/firefly /c/firefly
rm -r /c/firefly-wait
# ===================== New Version ========================
date
# ==========================================================
/c/firefly/run.bat 24680 KALIDASA >>$logfile 2>&1
