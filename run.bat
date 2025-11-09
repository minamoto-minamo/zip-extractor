@echo off
chcp 65001 >NUL
setlocal

set "JAR=%~dp0build\libs\zip-extractor.jar"

set "CONF=%~dp0config.properties"

echo [INFO] Running %JAR%
java -Xms64m -Xmx512m -jar "%JAR%" "%CONF%"
endlocal
