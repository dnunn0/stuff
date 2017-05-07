setlocal
if not "%JRE_HOME%"=="" goto JRE_SET
Echo JRE_HOME not set
goto :eof
:JRE_SET

Set app_dir=%~dp0
for %%* in (.) do set app_name=%%~nx*

Set ROBOCOPY_OK=3
del /s/q %app_dir%dist && rmdir /s/q %app_dir%dist
robocopy "%JRE_HOME%" "%app_dir%dist\%app_name%\runtime\jre" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done
robocopy "%app_dir%build\distributions" "%app_dir%dist\%app_name%\app" /E /PURGE /COPY:DAT /DCOPY:T /NS /NC /NFL /NDL /NP
IF %ERRORLEVEL% GTR %ROBOCOPY_OK% goto done

Set archive_name=%app_name%-1.0-SNAPSHOT

pushd %app_dir%dist\%app_name%\app 
"C:\Program Files (x86)\7-Zip\7z" x %archive_name%.zip 
robocopy "%archive_name%\bin" bin 
robocopy "%archive_name%\lib" lib && popd
rmdir %archive_name%
rm *.zip
rm *.tar
popd

Echo setlocal > dist\%app_name%\run.bat
Echo set PATH=runtime\jre\bin >> dist\%app_name%\run.bat
Echo app\bin\%app_name% %%*>> dist\%app_name%\run.bat

pushd dist && "C:\Program Files (x86)\7-Zip\7z" a -tzip -mx7 %app_name% && popd
