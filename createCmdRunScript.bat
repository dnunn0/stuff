Set app_dir=%~dp0
for %%* in (.) do set app_name=%%~nx*

if not "%1"=="" goto HAS_FILE
  @echo usage: %0 batch-file-name main-class
goto :eof

:HAS_FILE
set batchfile= %1

set mainClassName=com.whatgameapps.%app_name%.Main
if "%2"=="" goto HAS_MAIN
  set mainClassName=%2
:HAS_MAIN

Echo setlocal> %batchfile%
Echo Set app_dir=%%~dp0> %batchfile%
ECHO Set JAVA_EXE=%%app_dir%%runtime\jre\bin\java.exe>> %batchfile%
ECHO set PATH=>> %batchfile%
ECHO Set CLASSPATH="%%app_dir%%app\resources";"%%app_dir%%app";"%%app_dir%%app\lib\*">> %batchfile%
ECHO "%%JAVA_EXE%%" -server -Xmx8m -cp %%CLASSPATH%% %mainClassName% %%*>> %batchfile%

goto :eof