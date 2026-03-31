REM FeuerwehrManagementSystem - AlarmInfo starten mit Konsole
@Echo off

SET CURDIR=%~dp0

java -jar "%CURDIR%FeuerwehrManagementSystem.jar" EMailService
