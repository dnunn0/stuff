setlocal
if not "%JAVA_HOME%"=="" goto JRE_SET
Echo JAVA_HOME not set
goto :eof
:JRE_SET

Set app_dir=%~dp0
for %%* in (.) do set app_name=%%~nx*

Set ROBOCOPY_OK=3
CALL :DELETE_DIR %app_dir%dist
robocopy "%JAVA_HOME%\jre" "%app_dir%dist\%app_name%\runtime\jre" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

del "%app_dir%dist\%app_name%\runtime\jre"\Welcome.html

set FILE_LIST=(charsets deploy jce resources jfr jfxswt plugin management-agent )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\lib\%%i.jar 
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\ext
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\management

set FILE_LIST=(rmid rmiregistry tnameserv keytool kinit klist ktab policytool orbd servertool javaws javacpl jabswitch java-rmi jjs jp2launcher unpack200 )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.exe 

set FILE_LIST=(jfxwebkit awt msvcr100 msvcr120 msvcp120 javafx_font_t2k splashscreen javafx_iio)
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.dll 

CALL :DELETE_DIR  "%app_dir%dist\%app_name%\runtime\jre"\bin\client
CALL :DELETE_DIR  "%app_dir%dist\%app_name%\runtime\jre"\bin\dtplugin
CALL :DELETE_DIR  "%app_dir%dist\%app_name%\runtime\jre"\bin\plugin2

Set archive_name=%app_name%-1.0-SNAPSHOT
robocopy "%app_dir%build\distributions" "%app_dir%dist\%app_name%\app" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

pushd %app_dir%dist\%app_name%\app 
"C:\Program Files (x86)\7-Zip\7z" x %archive_name%.zip 
robocopy "%archive_name%\bin" bin 
robocopy "%archive_name%\lib" lib
popd
CALL :DELETE_DIR  %archive_name%
rm *.zip
rm *.tar
popd

set batchfile= dist\%app_name%\run.bat

Echo setlocal> %batchfile%
Echo Set app_dir=%%~dp0>> %batchfile%
Echo pushd %%app_dir%% >> %batchfile%
Echo Set JAVA_HOME=%%app_dir%%runtime\jre\>> %batchfile%
Echo set PATH=%%JAVA_HOME%%bin>> %batchfile%
Echo Set JAVA_OPTS=-server -Xmx8m >>%batchfile%
Echo rem Set DEBUG=y >>%batchfile%
Echo app\bin\%app_name% %%* >> %batchfile%

set batchfile= dist\%app_name%\run

Echo #!/bin/bash>> %batchfile%
Echo app_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )">> %batchfile%
Echo pushd $app_dir>> %batchfile%
Echo export JAVA_HOME=${app_dir}/runtime/jre/>> %batchfile%
Echo export PATH=${JAVA_HOME}bin:/usr/bin:/bin>> %batchfile%
Echo export JAVA_OPTS='-server -Xmx8m' >> %batchfile%
Echo #export DEBUG=y >> %batchfile%
Echo chmod 777 app/bin/%app_name% >> %batchfile%
Echo chmod 777 runtime/bin/* >> %batchfile%
Echo app/bin/%app_name% $ *>> %batchfile%

pushd dist && "C:\Program Files (x86)\7-Zip\7z" a -tzip -mx7 %app_name% && popd

goto :done

:DELETE_DIR
del /s/q %1 && rmdir /s/q %1
exit /b


:done