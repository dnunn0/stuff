Set app_dir=%~dp0
for %%* in (.) do set app_name=%%~nx*

set mainClassName=com.whatgameapps.%app_name%.Main
if "%1"=="" goto HAS_MAIN
  set mainClassName=%1
:HAS_MAIN

set batchfile= dist\%app_name%\run.bat

Echo setlocal> %batchfile%
Echo Set app_dir=%%~dp0> %batchfile%
ECHO Set JAVA_EXE=%%app_dir%%runtime\jre\bin\java.exe>> %batchfile%
ECHO set PATH=>> %batchfile%
ECHO Set CLASSPATH="%%app_dir%%app\resources";"%%app_dir%%app";"%%app_dir%%app\lib\*">> %batchfile%
ECHO "%%JAVA_EXE%%" -server -Xmx8m -cp %%CLASSPATH%% %mainClassName% %%*>> %batchfile%

goto :eof