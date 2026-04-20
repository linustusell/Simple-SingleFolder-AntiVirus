@echo off
setlocal

for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "$line = java -XshowSettings:properties -version 2>&1 | Select-String 'java.home'; if ($line) { $line.ToString().Split('=')[1].Trim() }"`) do set "JAVA_HOME=%%i"

if "%JAVA_HOME%"=="" (
  echo [ERROR] No s'ha pogut detectar JAVA_HOME automaticament.
  echo Revisa que `java -version` funcioni al teu sistema.
  goto :end
)

call "%~dp0mvnw.cmd" javafx:run
if errorlevel 1 (
  echo.
  echo [ERROR] No s'ha pogut arrencar l'aplicacio.
)

:end
endlocal
pause
