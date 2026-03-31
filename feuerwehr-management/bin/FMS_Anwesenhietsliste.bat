REM FeuerwehrManagementSystem - ANWESENHEITSLISTE DRUCKENstarten mit Konsole
@Echo off

SET CURDIR=%~dp0

java -jar "%CURDIR%FeuerwehrManagementSystem.jar" ANWESENHEITSLISTE_DRUCKEN
