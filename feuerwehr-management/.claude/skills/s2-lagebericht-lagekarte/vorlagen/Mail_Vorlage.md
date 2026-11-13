# E-MAIL LAGEBERICHT – Vorlage

Der Skill füllt die `{{…}}`-Felder aus der Lagemeldung aus und legt das Ergebnis als
`Mail_Lagebericht_Nr<lfd>_<Zeit>.md` ab. Nur Text zwischen den Linien in die Mail kopieren.
Bei **Übung**: Zeilen mit `[ÜBUNG]` übernehmen, sonst löschen.

---

**An:** fuehrungsstab@saarbruecken.de
**Betreff:** [ÜBUNG] ÜBUNG – ÜBUNG – ÜBUNG | Lagebericht Gemeinde Heusweiler Nr. {{lfd_nr}} – {{zeit}}
**Anhänge:**
- Lagebericht_Heusweiler_{{zeit}}_V{{lfd_nr}}.xlsx
- {{anhang_karte}} <!-- Lagekarte_Heusweiler_{{zeit}}_V{{lfd_nr}}_Folie1.pdf ODER Foto der analogen Lagekarte -->

---

[ÜBUNG] +++ ÜBUNG – ÜBUNG – ÜBUNG +++

Sehr geehrte Damen und Herren,

anbei der Lagebericht Nr. {{lfd_nr}} der TEL Gemeinde Heusweiler, Stand {{zeit}}, mit Lagekarte{{karte_hinweis}}.

**Lage in Kürze:** {{lage_kurz}}

**Lagebeurteilung Schadenslage:** {{stufe_text}} – Prognose: {{prognose}}

**Dringende Probleme / Bedarfe:**
{{probleme}}

Zeitkritische Nachforderungen wurden zusätzlich über Funk abgesetzt.

Nächster Lagebericht: {{naechster_bericht}}

Mit freundlichen Grüßen
TEL Gemeinde Heusweiler – S2
{{bearbeiter}}
Erreichbar: {{erreichbarkeit}}

[ÜBUNG] +++ ÜBUNG – ÜBUNG – ÜBUNG +++

---

## Ausfüllregeln (für den Skill)
- `lfd_nr`, `zeit`, `bearbeiter`: aus Kopf der Lagemeldung.
- `anhang_karte`: Dateiname der Lagekarten-PDF; bei „Lagekarte als: Foto“ → „Foto der analog geführten Lagekarte“.
- `karte_hinweis`: leer; bei Foto → „ (als Foto der analog geführten Karte)“.
- `lage_kurz`: 1–2 Sätze aus „Allgemein“ + Schwerpunkt-EA der Kurzfassung.
- `stufe_text`: 1 = geringfügig, 2 = erheblich, 3 = kritisch. `prognose`: Entspannung / Stagnation / Verschärfung.
- `probleme`: nummerierte Liste aus Abschnitt 9; keine Probleme → „keine“.
- `naechster_bericht`: nächste volle Stunde als taktische Zeit.
- `erreichbarkeit`: Telefon/Funkrufname der TEL, sobald bekannt (sonst Zeile löschen).
- Übung: `[ÜBUNG]`-Markierungen entfernen, Zeilen behalten. Echteinsatz: Zeilen mit `[ÜBUNG]` komplett löschen.
