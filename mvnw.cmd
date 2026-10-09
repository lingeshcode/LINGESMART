@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
@setlocal

set ERROR_CODE=0

@REM Find the project base dir
set MAVEN_PROJECTBASEDIR=%~dp0
if "%MAVEN_PROJECTBASEDIR:~-1%"=="\" set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%

set MAVEN_CONFIG=.mvn

@REM Execute Maven Wrapper or fallback to mvn
powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip', '%TEMP%\maven.zip') }"
if not exist "%TEMP%\maven-bin" (
    powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%TEMP%\maven-bin' -Force"
)

for /d %%i in ("%TEMP%\maven-bin\apache-maven-*") do set MAVEN_HOME=%%i

"%MAVEN_HOME%\bin\mvn.cmd" %*
