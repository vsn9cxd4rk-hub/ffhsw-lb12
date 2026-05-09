/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen;

import ao.listen.AnwesenheitListeOptionenAO;
import data.DatenbankZugriff;
import go.Anwesenheit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleAnwesenheit {
    public void insert(Anwesenheit anwesenheit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO anwesenheit (`id`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `mitgliederID` , `mandantID`) VALUES ('" + anwesenheit.getId() + "', '" + anwesenheit.getJahr() + "', '" + anwesenheit.getVeranstaltungID() + "', '" + anwesenheit.getVeranstaltungKategorie() + "', '" + anwesenheit.getMitgliederID() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void insertArray(Anwesenheit[] anwesenheit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        StringBuilder build = new StringBuilder();
        int a = 0;
        while (a < anwesenheit.length) {
            build.append("(");
            build.append(anwesenheit[a].getId());
            build.append(",");
            build.append(anwesenheit[a].getJahr());
            build.append(",");
            build.append(anwesenheit[a].getVeranstaltungID());
            build.append(",");
            build.append(anwesenheit[a].getVeranstaltungKategorie());
            build.append(",");
            build.append(anwesenheit[a].getMitgliederID());
            build.append(",");
            build.append(runApplication.PROPERTIES.get("MandantID"));
            if (a != anwesenheit.length - 1) {
                build.append("),");
            } else {
                build.append(");");
            }
            ++a;
        }
        String sql = "INSERT INTO anwesenheit (`id`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `mitgliederID` , `mandantID`) VALUES " + build.toString();
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(int mitgliederID, int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from anwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from anwesenheit where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getNichtAnwesendeMitglieder(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("Select m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id != a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("Select m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id != a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getNichtInFahrzeugeinteilung(int veranstaltungID, String[] mitgliederIDs) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        StringBuilder build = new StringBuilder();
        int i = 0;
        while (i < mitgliederIDs.length) {
            build.append(mitgliederIDs[i]);
            build.append(",");
            ++i;
        }
        logging.logSQL((Object)("SELECT m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID not in (" + build.substring(0, build.length() - 1) + ") and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID not in (" + build.substring(0, build.length() - 1) + ") and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public int getAnwesendStatus(int mitgliederID, int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM anwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM anwesenheit;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM anwesenheit;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getGesamtBeteiligung(int mitgliederID, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getGesamtVeranstaltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `anwesenheit` WHERE `veranstaltungID` = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `veranstaltungID` = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getGesamtVeranstaltungByKategorie(int kategorieID, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `anwesenheit` WHERE veranstaltungKategorie = " + kategorieID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE veranstaltungKategorie = " + kategorieID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getBeteiligungByKategorie(int mitgliederID, int veranstaltungKategorie, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie = " + veranstaltungKategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie = " + veranstaltungKategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getBeteiligungByKategorieGroesser3(int mitgliederID, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie > 3 and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie > 3 and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getBeteiligungEinsatzDienst(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie IN (1,2) and jahr = " + SbcUtils.timeStamp((String)"yyyy") + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie IN (1,2) and jahr = " + SbcUtils.timeStamp((String)"yyyy") + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Vector<Vector<String>> getAllDataForList() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.ausserDienst = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.ausserDienst = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getFilterDataForList(String[] selektierteMitglieder) throws SQLException {
        StringBuilder build = new StringBuilder();
        build.append("(");
        int i = 0;
        while (i < selektierteMitglieder.length) {
            build.append(selektierteMitglieder[i]);
            if (i != selektierteMitglieder.length - 1) {
                build.append(",");
            }
            ++i;
        }
        build.append(")");
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.id in " + build.toString() + " and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.id in " + build.toString() + " and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector2(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getAnwesendeMitgliederEinerVeranstaltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung, m.name, m.vorname FROM anwesenheit a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN dienstgrad d ON d.id = m.dienstgrad WHERE veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM anwesenheit a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN dienstgrad d ON d.id = m.dienstgrad WHERE veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " ;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector2(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<String> getAnwesendeMitgliederByVeranstaltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<Integer> getAnwesendeMitgliederIDByVeranstaltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.id FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.id FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAnwesendeMitglieder(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getAnwesendeMitgliederEMail(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.email FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.eMailDeaktiv = 0 and eMail != '' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.email FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.eMailDeaktiv = 0 and eMail != '' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getZFCountMitVeranstaltungsID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 20 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 20 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getGFCountMitVeranstaltungsID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 19 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 19 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getFMCountMitVeranstaltungsID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 6 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 6 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Vector<Vector<String>> getAlleAnwesendeVeranstaltungenProMitglied(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT v.datum, v.zeit, v.zeitEnde, v.name2 FROM anwesenheit a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id WHERE a.mitgliederID = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;"));
        ResultSet result = statement.executeQuery("SELECT v.datum, v.zeit, v.zeitEnde, v.name2 FROM anwesenheit a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id WHERE a.mitgliederID = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector3(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> anwesenheitListe = new Vector<String>();
        if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
            anwesenheitListe.add(result.getString("beschreibung"));
        }
        anwesenheitListe.add(result.getString("name"));
        anwesenheitListe.add(result.getString("vorname"));
        anwesenheitListe.add(" ");
        return anwesenheitListe;
    }

    private Vector<String> mapResultSetToVector2(ResultSet result) throws SQLException {
        Vector<String> anwesenheitListe = new Vector<String>();
        if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
            anwesenheitListe.add(result.getString("beschreibung"));
        }
        anwesenheitListe.add(result.getString("name"));
        anwesenheitListe.add(result.getString("vorname"));
        return anwesenheitListe;
    }

    public Vector<String> createHeadnameForFilter() {
        Vector<String> headname = new Vector<String>();
        if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
            headname.add("Dienstgrad");
        }
        headname.add("Name");
        headname.add("Vorname");
        if (AnwesenheitListeOptionenAO.zusatzBox1.isSelected()) {
            headname.add(AnwesenheitListeOptionenAO.zusatzFeld1.getText());
        }
        if (AnwesenheitListeOptionenAO.zusatzBox2.isSelected()) {
            headname.add(AnwesenheitListeOptionenAO.zusatzFeld2.getText());
        }
        if (AnwesenheitListeOptionenAO.zusatzBox3.isSelected()) {
            headname.add(AnwesenheitListeOptionenAO.zusatzFeld3.getText());
        }
        return headname;
    }

    private Vector<String> mapResultSetToVector3(ResultSet result) throws SQLException {
        Vector<String> anwesenheitListe = new Vector<String>();
        anwesenheitListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
        anwesenheitListe.add(result.getString("zeit"));
        anwesenheitListe.add(result.getString("zeitEnde"));
        anwesenheitListe.add(result.getString("name2"));
        return anwesenheitListe;
    }
}

