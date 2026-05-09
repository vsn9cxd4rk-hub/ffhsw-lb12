/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import ao.listen.LehrgangListeOptionenAO;
import data.DatenbankZugriff;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleLehrgang {
    public Vector<Vector<String>> getAllDataForList() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        int anzahlLehrgaenge = new TabelleLehrgang_kategorie().getCount();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result, anzahlLehrgaenge));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result, int anzahlLehrgaenge) throws SQLException {
        Vector<String> lehrgangListe = new Vector<String>();
        lehrgangListe.add(result.getString("beschreibung"));
        lehrgangListe.add(result.getString("name"));
        lehrgangListe.add(result.getString("vorname"));
        int[] mustBeSelected = new TabelleMitglieder_laufbahn().getLehrgangData(result.getInt("id"));
        int i = 0;
        while (i < anzahlLehrgaenge) {
            if (mustBeSelected[i] == 1) {
                lehrgangListe.add("X");
            } else {
                lehrgangListe.add(" ");
            }
            ++i;
        }
        return lehrgangListe;
    }

    public Vector<Vector<String>> getFilterDataForList(int[] lehrgangKategorieIDs) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        int anzahlLehrgaenge = lehrgangKategorieIDs.length;
        while (result.next()) {
            liste.add(this.mapFilteredResultSetToVector(result, anzahlLehrgaenge, lehrgangKategorieIDs));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapFilteredResultSetToVector(ResultSet result, int anzahlLehrgaenge, int[] lehrgangKategorieIDs) throws SQLException {
        Vector<String> lehrgangListe = new Vector<String>();
        lehrgangListe.add(result.getString("beschreibung"));
        lehrgangListe.add(result.getString("name"));
        lehrgangListe.add(result.getString("vorname"));
        int[] mustBeSelected = new TabelleMitglieder_laufbahn().getLehrgangData(result.getInt("id"), lehrgangKategorieIDs);
        TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
        int i = 0;
        while (i < anzahlLehrgaenge) {
            if (mustBeSelected[i] == 1) {
                lehrgangListe.add("X");
            } else {
                lehrgangListe.add(" ");
            }
            ++i;
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[0].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG25(result.getInt("id"))));
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[1].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG26(result.getInt("id"))));
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[2].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG30(result.getInt("id"))));
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[3].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG41(result.getInt("id"))));
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[4].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG42(result.getInt("id"))));
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[5].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufLKW(result.getInt("id"))));
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[6].isSelected()) {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtTraining(result.getInt("id"))));
        }
        return lehrgangListe;
    }

    public Vector<String> mapHeadNameToVector() throws SQLException {
        Vector<String> lehrgangListe = new Vector<String>();
        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
        String[] lehrgangKategorie = Utils.listToArray(tabLehrgangKategorie.getAlleLehrg\u00e4nge());
        lehrgangListe.add("Dienstgrad");
        lehrgangListe.add("Name");
        lehrgangListe.add("Vorname");
        int i = 0;
        while (i < lehrgangKategorie.length) {
            lehrgangListe.add(lehrgangKategorie[i]);
            ++i;
        }
        return lehrgangListe;
    }

    public Vector<String> mapFilterHeadNameToVector(int[] lehrgangKategorieIds) throws SQLException {
        Vector<String> lehrgangListe = new Vector<String>();
        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
        String[] lehrgangKategorie = Utils.listToArray(tabLehrgangKategorie.getFilterNameLehrgang(lehrgangKategorieIds));
        lehrgangListe.add("Dienstgrad");
        lehrgangListe.add("Name");
        lehrgangListe.add("Vorname");
        int i = 0;
        while (i < lehrgangKategorie.length) {
            lehrgangListe.add(lehrgangKategorie[i]);
            ++i;
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[0].isSelected()) {
            lehrgangListe.add("G25");
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[1].isSelected()) {
            lehrgangListe.add("G26/3");
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[2].isSelected()) {
            lehrgangListe.add("G30");
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[3].isSelected()) {
            lehrgangListe.add("G41");
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[4].isSelected()) {
            lehrgangListe.add("G42");
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[5].isSelected()) {
            lehrgangListe.add("Ablauf LKW F\u00fchrerschein");
        }
        if (LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[6].isSelected()) {
            lehrgangListe.add("AGT Training");
        }
        if (LehrgangListeOptionenAO.zusatzBox1.isSelected()) {
            lehrgangListe.add(LehrgangListeOptionenAO.zusatzFeld1.getText());
        }
        if (LehrgangListeOptionenAO.zusatzBox2.isSelected()) {
            lehrgangListe.add(LehrgangListeOptionenAO.zusatzFeld2.getText());
        }
        if (LehrgangListeOptionenAO.zusatzBox3.isSelected()) {
            lehrgangListe.add(LehrgangListeOptionenAO.zusatzFeld3.getText());
        }
        return lehrgangListe;
    }
}

