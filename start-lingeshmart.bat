@echo off
echo ===================================================
echo   Starting LingeshMart Application (Windows Native)
echo ===================================================

start "LingeshMart Web Server" powershell -ExecutionPolicy Bypass -File server.ps1

echo Waiting for server startup...
timeout /t 2 /nobreak >nul

echo Opening LingeshMart in Chrome...
start chrome "http://localhost:8080/lingeshmart"

echo ===================================================
echo   LingeshMart is LIVE at http://localhost:8080/lingeshmart
echo ===================================================
