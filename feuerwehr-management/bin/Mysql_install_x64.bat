@echo off
SET CURDIR=%~dp0

REM Installation der Datenbank
echo Installing MySQL Server. Please wait...
msiexec /i "%CURDIR%install\mysql-5.5.28-win32.msi" /qn

REM Starte DB Service
echo Service install...
"C:\Program Files (x86)\MySQL\MySQL Server 5.5\bin\mysqld.exe" --install
net start mysql

REM Konfiguriere Datenbank
echo Configurating MySQL Server...
"C:\Program Files (x86)\MySQL\MySQL Server 5.5\bin\mysqlinstanceconfig.exe" -i -q ServiceName=MySQL RootPassword=root_fms ServerType=SERVER DatabaseType=INNODB Port=3306 Charset=utf8


REM FERTIG / ENDE
echo Installation was successfully

