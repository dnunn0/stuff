setlocal

if not "%JAVA_HOME%"=="" goto JAVA_HOME_SET
    Echo JAVA_HOME not set
    goto :eof
:JAVA_HOME_SET

Set app_dir=%~dp0
for %%* in (.) do set app_name=%%~nx*

Set ROBOCOPY_OK=3
CALL :DELETE_DIR %app_dir%dist
robocopy "%JAVA_HOME%\jre" "%app_dir%dist\%app_name%\runtime\jre" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

CALL :SHRINK_JRE

Set archive_name=%app_name%-1.0-SNAPSHOT
robocopy "%app_dir%build\distributions" "%app_dir%dist\%app_name%\app" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

pushd %app_dir%dist\%app_name%\app 
"C:\Program Files (x86)\7-Zip\7z" x %archive_name%.zip 
robocopy "%archive_name%\bin" bin 
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done
robocopy "%archive_name%\lib" lib
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done
CALL :DELETE_DIR  %archive_name%
CALL :DELETE_DIR  bin
rm *.zip
rm *.tar
rem now extract app out of app jar, since executable jar is useless when there's other jars required
"C:\Program Files (x86)\7-Zip\7z" x lib\%archive_name%.jar
mkdir resources
move public resources
del /s/q lib\%archive_name%.jar
for /f "tokens=1-2" %%a in (META-INF\MANIFEST.MF) do (
    if "%%a"=="Main-Class:" set mainClassName=%%b
)

if NOT "%mainClassName%"=="" goto HAS_MAIN
    Echo this script requires %archive_name%.jar include a manifest with the main class
Goto :eof

:HAS_MAIN
CALL :DELETE_DIR META-INF

rem robocopy %app_dir%build\resources\main\public resources\public
popd


set batchfile= dist\%app_name%\run.bat

Echo setlocal> %batchfile%
Echo Set app_dir=%%~dp0>> %batchfile%
ECHO Set JAVA_EXE=%%app_dir%%runtime\jre\bin\java.exe>> %batchfile%
ECHO set PATH=>> %batchfile%
ECHO Set CLASSPATH="%%app_dir%%app\resources";"%%app_dir%%app";"%%app_dir%%app\lib\*">> %batchfile%
ECHO "%%JAVA_EXE%%" -server -Xmx8m -cp %%CLASSPATH%% %mainClassName% %%*>> %batchfile%


set batchfile= dist\%app_name%\run

Echo #!/bin/bash>> %batchfile%
Echo app_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" > /dev/null && pwd )">> %batchfile%
Echo JAVA_BIN=${app_dir}/runtime/jre/bin>> %batchfile%
Echo PATH=${JAVA_BIN}:/usr/bin:/bin>> %batchfile%
ECHO CLASSPATH=${app_dir}/app/resources:${app_dir}/app:${app_dir}/app/lib/*>> %batchfile%
Echo %JAVA_BIN/java -server -Xmx8m -cp $CLASSPATH %mainClassName% %%*>> %batchfile%

pushd dist && "C:\Program Files (x86)\7-Zip\7z" a -tzip -mx7 %app_name% && popd

goto :done

:DELETE_DIR
del /s/q %1 && rmdir /s/q %1
exit /b

:SHRINK_JRE
rem see http://www.oracle.com/technetwork/java/javase/jre-8-readme-2095710.html
del "%app_dir%dist\%app_name%\runtime\jre"\Welcome.html

set FILE_LIST=(charsets deploy jce resources javaws jfr jfxswt plugin management-agent )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\lib\%%i.jar 

set FILE_LIST=(hijrah-config-umalqura sound. javafx calendars fontconfig.src psfontj2d )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\lib\%%i.properties

set FILE_LIST=(classlist )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\lib\%%i

del "%app_dir%dist\%app_name%\runtime\jre"\lib\deploy\messages_*.properties
del "%app_dir%dist\%app_name%\runtime\jre"\lib\deploy\splash*.gif
del "%app_dir%dist\%app_name%\runtime\jre"\lib\deploy\*.zip

CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\applet
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\cmm
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\ext
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\fonts
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\images
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\jfr
CALL :DELETE_DIR "%app_dir%dist\%app_name%\runtime\jre"\lib\management

set FILE_LIST=(rmid rmiregistry tnameserv keytool kinit klist ktab policytool orbd pack200)
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.exe 
set FILE_LIST=(servertool ssvagent javaw javaws javacpl jabswitch java-rmi jjs jp2launcher unpack200 )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.exe 

rem need at least one of (not sure) msvcr100 msvcr120 msvcp120 for 32-bit java to work in 64-bit OS
set FILE_LIST=(awt decora_sse fxplugins glass glib-lite gstreamer-lite hprof hprof javafx_font javafx_font_t2k )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.dll 
set FILE_LIST=(javafx_iio jfxwebkit prism_common prism_d3d prism_sw splashscreen WindowsAccessBridge-32)
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.dll 
set FILE_LIST=(fontmanager instrument j2pcsc j2pkcs11 java_crw_demo JavaAccessBridge-32 javacpl jawt JAWTAccessBridge-32 )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.dll 
set FILE_LIST=(jfr jfxmedia jp2iexp jp2native jp2ssv jsound jsoundds management mlib_image sunmscapi w2k_lsa_auth )
for %%i in %FILE_LIST% do del "%app_dir%dist\%app_name%\runtime\jre"\bin\%%i.dll 


CALL :DELETE_DIR  "%app_dir%dist\%app_name%\runtime\jre"\bin\client
CALL :DELETE_DIR  "%app_dir%dist\%app_name%\runtime\jre"\bin\dtplugin
CALL :DELETE_DIR  "%app_dir%dist\%app_name%\runtime\jre"\bin\plugin2
exit /b


:done