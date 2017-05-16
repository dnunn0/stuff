setlocal
echo ====================================================================
echo ====================================================================
echo ====================================================================

if not "%JAVA_HOME%"=="" goto JAVA_HOME_SET
    Echo JAVA_HOME not set
    goto :eof
:JAVA_HOME_SET

Set app_dir=%~dp0
for %%* in (.) do set app_name=%%~nx*
Set dist_dir=%app_dir%distribute\
Set dist_app_dir=%dist_dir%%app_name%\

Set ROBOCOPY_OK=3
CALL :DELETE_DIR %dist_dir% || Exit /b 1


robocopy "%JAVA_HOME%\jre" "%dist_app_dir%runtime\jre" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

CALL :SHRINK_JRE   || Exit /b 1

Set archive_name=%app_name%-1.0-SNAPSHOT
robocopy "%app_dir%build\distributions" "%dist_app_dir%app" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

pushd %dist_app_dir%app 
"C:\Program Files (x86)\7-Zip\7z" x %archive_name%.zip 
robocopy "%archive_name%\bin" bin 
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done
robocopy "%archive_name%\lib" lib
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done
CALL :DELETE_DIR  %archive_name%  || Exit /b 1
CALL :DELETE_DIR  bin || Exit /b 1
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
CALL :DELETE_DIR META-INF || Exit /b 1

rem robocopy %app_dir%build\resources\main\public resources\public
popd

call createCmdRunScript.bat %dist_app_dir%run.bat %mainClassName%  || Exit /b 1

call createBashRunScript.bat %dist_app_dir%run.sh %mainClassName%  || Exit /b 1

pushd %dist_dir% && "C:\Program Files (x86)\7-Zip\7z" a -tzip -mx7 %app_name% && popd

if "%1"=="" goto done
copy /Y %dist_dir%\%app_name%.zip %1

dir %1

goto :done

:DELETE_DIR
del /s/q %1 && rmdir /s/q %1
if not exist %1 exit /b 0
dir %1
set errorlevel=1
exit /b %errorlevel%



:SHRINK_JRE
rem see http://www.oracle.com/technetwork/java/javase/jre-8-readme-2095710.html
del "%dist_app_dir%runtime\jre"\Welcome.html

set FILE_LIST=(charsets deploy jce resources javaws jfr jfxswt plugin management-agent )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\lib\%%i.jar 

set FILE_LIST=(hijrah-config-umalqura sound. javafx calendars fontconfig.src psfontj2d )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\lib\%%i.properties

set FILE_LIST=(classlist )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\lib\%%i

del "%dist_app_dir%runtime\jre"\lib\deploy\messages_*.properties
del "%dist_app_dir%runtime\jre"\lib\deploy\splash*.gif
del "%dist_app_dir%runtime\jre"\lib\deploy\*.zip

CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\applet  || Exit /b 1
CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\cmm || Exit /b 1
CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\ext || Exit /b 1
CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\fonts || Exit /b 1
CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\images || Exit /b 1
CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\jfr || Exit /b 1
CALL :DELETE_DIR "%dist_app_dir%runtime\jre"\lib\management || Exit /b 1

set FILE_LIST=(rmid rmiregistry tnameserv keytool kinit klist ktab policytool orbd pack200)
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\bin\%%i.exe 
set FILE_LIST=(servertool ssvagent javaw javaws javacpl jabswitch java-rmi jjs jp2launcher unpack200 )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\bin\%%i.exe 

rem need at least one of (not sure) msvcr100 msvcr120 msvcp120 for 32-bit java to work in 64-bit OS
set FILE_LIST=(awt decora_sse fxplugins glass glib-lite gstreamer-lite hprof hprof javafx_font javafx_font_t2k )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\bin\%%i.dll 
set FILE_LIST=(javafx_iio jfxwebkit prism_common prism_d3d prism_sw splashscreen WindowsAccessBridge-32)
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\bin\%%i.dll 
set FILE_LIST=(fontmanager instrument j2pcsc j2pkcs11 java_crw_demo JavaAccessBridge-32 javacpl jawt JAWTAccessBridge-32 )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\bin\%%i.dll 
set FILE_LIST=(jfr jfxmedia jp2iexp jp2native jp2ssv jsound jsoundds management mlib_image sunmscapi w2k_lsa_auth )
for %%i in %FILE_LIST% do del "%dist_app_dir%runtime\jre"\bin\%%i.dll 


CALL :DELETE_DIR  "%dist_app_dir%runtime\jre"\bin\client || Exit /b 1
CALL :DELETE_DIR  "%dist_app_dir%runtime\jre"\bin\dtplugin || Exit /b 1
CALL :DELETE_DIR  "%dist_app_dir%runtime\jre"\bin\plugin2 || Exit /b 1
exit /b


:done
exit /b %errorlevel%