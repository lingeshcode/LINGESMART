@echo off
title LingeshMart Public Cloudflare Link Generator
cd /d "%~dp0"

if exist cloudflared.exev ren cloudflared.exev cloudflared.exe
if exist cloudflared-windows-amd64.exe ren cloudflared-windows-amd64.exe cloudflared.exe

echo ===================================================
echo   1. Starting LingeshMart Local Server...
echo ===================================================
start "LingeshMart Local Server" powershell -NoProfile -ExecutionPolicy Bypass -File server.ps1

echo.
echo ===================================================
echo   2. Connecting to Cloudflare Tunnel...
echo   Please wait 5-10 seconds for your link to appear!
echo ===================================================
echo.

cloudflared.exe tunnel --url http://localhost:8080

echo.
pause
