@echo off
setlocal

for /f "tokens=2 delims==" %%a in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr "java.home"') do set "JAVA_HOME=%%a"
set "JAVA_HOME=%JAVA_HOME: =%"

call "%~dp0mvnw.cmd" -q javafx:run

endlocal
pause
