@echo off
setlocal

set JAR=%~dp0dsp408-proxy-0.1.0-SNAPSHOT-all.jar
set DSP_IP=192.168.0.166

if not exist "%JAR%" (
    echo JAR not found:
    echo %JAR%
    pause
    exit /b 1
)

echo Starting DSP408 Proxy...
echo JAR      : %JAR%
echo DSP-IP   : %DSP_IP%
echo.

java -jar "%JAR%" ^
  --listen-host 127.0.0.1 ^
  --listen-port 9761 ^
  --target-host %DSP_IP% ^
  --target-port 9761 ^
  --log-dir proxy_logs ^
  --stream-host 127.0.0.1 ^
  --stream-port 19081 ^
  --control-host 127.0.0.1 ^
  --control-port 19082

echo.
echo Proxy stopped.
pause
