@echo off
SET CURDIR=%~dp0

REM Deinstallation der Datenbank
echo Deinstalling MySQL Server. Please wait...
msiexec /uninstall "%CURDIR%install\mysql-5.5.28-win32.msi" /quiet

echo Deinstallation MySQL Dienst
sc delete mysql

echo Entfernen der Einsatllungen
del /q "%CURDIR%properties\db.properties"

REM FERTIG / ENDE
echo Deinstallation was successfully

