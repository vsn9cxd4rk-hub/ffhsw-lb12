# FeuerwehrManagementSystem

Dieses Projekt enthält dekompilierten Java-Code aus dem ursprünglichen .jar-Archiv und ist als Maven-Projekt strukturiert. Die Quellcodes liegen unter `src/java`.

## Projektstruktur
- src/java: Enthält alle Java-Quellcodes
- pom.xml: Maven Build-Konfiguration

## Kompilieren und Ausführen
1. Stelle sicher, dass Maven installiert ist.
2. Führe im Projektverzeichnis aus:
   ```
   mvn compile
   ```
3. Zum Ausführen des Programms:
   ```
   mvn exec:java -Dexec.mainClass="<Hauptklasse>"
   ```
   Ersetze `<Hauptklasse>` durch den Namen der Main-Klasse.

## Hinweise
- Die dekompilierten Quellen können Fehler enthalten. Überprüfe und bereinige den Code ggf. manuell.
- Weitere Anpassungen (z.B. Tests, Dokumentation) sind möglich.
