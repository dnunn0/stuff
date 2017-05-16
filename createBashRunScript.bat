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

Echo #!/bin/bash> %batchfile%
Echo app_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )">> %batchfile%
Echo JAVA_BIN=${app_dir}/runtime/jre/bin>> %batchfile%
Echo PATH=${JAVA_BIN}:/usr/bin:/bin>> %batchfile%
ECHO CLASSPATH=$app_dir/app/resources:$app_dir/app:$app_dir/app/lib/*>> %batchfile%
Echo $JAVA_BIN/java -server -Xmx8m -cp $CLASSPATH %mainClassName% $*>> %batchfile%

goto :eof