/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import data.tabellen.TabelleLehrgang_kategorie;
import go.Mitgliederlaufbahn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleMitglieder_laufbahn {
    public static Vector<String> headnameLehrgang = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Lehrgang");
            this.add("Datum von");
            this.add("Datum bis (Pr\u00fcfungsdatum)");
        }
    };
    public static Vector<String> headnameDienstgrad = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum");
            this.add("Bef\u00f6rderung");
        }
    };
    public static Vector<String> headnameF\u00fchrerschein = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("F\u00fchrerschein");
            this.add("Pr\u00fcfungsdatum");
        }
    };
    public static Vector<String> headnameFunktion = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum von");
            this.add("Datum bis");
            this.add("Funktion in der Feuerwehr");
            this.add("Funktion au\u00dferhalb der Feuerwehr");
        }
    };
    public static Vector<String> headnameEhrung = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum");
            this.add("Ehrung");
            this.add("Abzeichen");
        }
    };

    public void insert(Mitgliederlaufbahn laufbahn) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder_laufbahn (`id`,`mitgliederID`,`datumVon`,`datum`,`art`, `alterDienstgrad`, `neuerDienstgrad`,`lehrgang`,`UE`, `mandantID`) VALUES ('" + laufbahn.getId() + "', '" + laufbahn.getMitgliederID() + "', '" + laufbahn.getDatumVon() + "', '" + laufbahn.getDatum() + "', '" + laufbahn.getArt() + "', '" + laufbahn.getAlterDienstgrad() + "', '" + laufbahn.getNeuerDienstgrad() + "', '" + laufbahn.getLehrgang() + "', '" + laufbahn.getUe() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Mitgliederlaufbahn laufbahn) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_laufbahn set datumVon = '" + laufbahn.getDatumVon() + "', datum = '" + laufbahn.getDatum() + "', neuerDienstgrad = '" + laufbahn.getNeuerDienstgrad() + "', lehrgang = '" + laufbahn.getLehrgang() + "', ue = '" + laufbahn.getUe() + "' where id = " + laufbahn.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(Mitgliederlaufbahn laufbahn) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Delete from mitglieder_laufbahn where mitgliederID = " + laufbahn.getMitgliederID() + " and datumVon = '" + laufbahn.getDatumVon() + "' and datum = '" + laufbahn.getDatum() + "' and art = '" + laufbahn.getArt() + "' and neuerDienstgrad = " + laufbahn.getNeuerDienstgrad() + " and lehrgang = " + laufbahn.getLehrgang() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNumber() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM mitglieder_laufbahn;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM mitglieder_laufbahn;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getLetzteBefoerderung(int mitglierderID, int aktuelleDienstgrad) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT datum FROM mitglieder_laufbahn where mitgliederID = " + mitglierderID + " and neuerDienstgrad = " + aktuelleDienstgrad + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT datum FROM mitglieder_laufbahn where mitgliederID = " + mitglierderID + " and neuerDienstgrad = " + aktuelleDienstgrad + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return "";
    }

    public int getLehrgangCount(int mitgliederID, int lehrgangID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getIDListe(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT l.id from mitglieder_laufbahn l LEFT JOIN lehrgang_kategorie lk ON l.lehrgang =  lk.id LEFT JOIN dienstgrad d ON l.neuerDienstgrad = d.id where l.mitgliederID = " + mitgliederID + " and l.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by l.datum ;");
        logging.logSQL((Object)("SELECT l.id from mitglieder_laufbahn l LEFT JOIN lehrgang_kategorie lk ON l.lehrgang =  lk.id LEFT JOIN dienstgrad d ON l.neuerDienstgrad = d.id where l.mitgliederID = " + mitgliederID + " and l.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by l.datum ;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getNichtBestandeneListe(int mitgliederID, String filter) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("Select name from lehrgang_kategorie where id not in (SELECT lehrgang from mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ") and art in (" + filter + ") and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("Select name from lehrgang_kategorie where id not in (SELECT lehrgang from mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ") and art in (" + filter + ") and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int[] getLehrgangData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F', 'L', 'F\u00fc', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F', 'L', 'F\u00fc', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleLehrg\u00e4ngeID());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    continue block0;
                }
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getLehrgangSeminarData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('L', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('L', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleLehrg\u00e4ngeSeminarID());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    continue block0;
                }
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getF\u00fchrerscheinData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F\u00fc') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F\u00fc') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleF\u00fchrerscheinID());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    continue block0;
                }
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getFunktionData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleFunktionenID());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    continue block0;
                }
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getFunktionAu\u00dferhalbData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F_Au\u00dferhalb') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('F_Au\u00dferhalb') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleFunktionenAu\u00dferhalbID());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                System.out.println(i);
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    System.out.println("status: 1");
                    continue block0;
                }
                System.out.println("status: 0");
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getEhrungenData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('EH') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('EH') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleEhrungenIDs());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                System.out.println(i);
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    System.out.println("status: 1");
                    continue block0;
                }
                System.out.println("status: 0");
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getAbzeichenData(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('AB') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and lehrgang != 0 and art in ('AB') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] lehrgangListe = Utils.listToIntArray(new TabelleLehrgang_kategorie().getAlleAbzeichenIDs());
        int[] statusListe = new int[lehrgangListe.length];
        block0: while (result.next()) {
            int i = 0;
            while (i < lehrgangListe.length) {
                if (lehrgangListe[i] == result.getInt(1)) {
                    statusListe[i] = 1;
                    continue block0;
                }
                ++i;
            }
        }
        return statusListe;
    }

    public int[] getLehrgangData(int mitgliederID, int[] lehrgangKategorieIDs) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        StringBuilder build = new StringBuilder();
        build.append("(");
        int i = 0;
        while (i < lehrgangKategorieIDs.length) {
            build.append(lehrgangKategorieIDs[i]);
            if (i != lehrgangKategorieIDs.length - 1) {
                build.append(",");
            }
            ++i;
        }
        build.append(")");
        logging.logSQL((Object)("SELECT ml.lehrgang FROM mitglieder_laufbahn ml LEFT JOIN lehrgang_kategorie lk ON ml.lehrgang = lk.id where lk.id in " + build.toString() + " and ml.mitgliederID = " + mitgliederID + " and ml.lehrgang != 0 and ml.art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and ml.mandantID = 1;"));
        ResultSet result = statement.executeQuery("SELECT ml.lehrgang FROM mitglieder_laufbahn ml LEFT JOIN lehrgang_kategorie lk ON ml.lehrgang = lk.id where lk.id in " + build.toString() + " and ml.mitgliederID = " + mitgliederID + " and ml.lehrgang != 0 and ml.art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and ml.mandantID = 1;");
        int[] liste = new int[lehrgangKategorieIDs.length];
        block1: while (result.next()) {
            int i2 = 0;
            while (i2 < lehrgangKategorieIDs.length) {
                if (lehrgangKategorieIDs[i2] == result.getInt(1)) {
                    liste[i2] = 1;
                    continue block1;
                }
                ++i2;
            }
        }
        return liste;
    }

    public int getCountOfLehrgang(int lehrgangID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_laufbahn where lehrgang = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_laufbahn where lehrgang = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfLehrgangByMitglied(int lehrgangID, int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_laufbahn where lehrgang = " + lehrgangID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_laufbahn where lehrgang = " + lehrgangID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfLehrgangForBefoerderung(int mitgliederID, int[] lehrgaenge) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        StringBuilder build = new StringBuilder();
        int i = 0;
        while (i < lehrgaenge.length) {
            build.append(lehrgaenge[i]);
            if (i != lehrgaenge.length - 1) {
                build.append(",");
            }
            ++i;
        }
        if (build.length() == 0) {
            return 0;
        }
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_laufbahn WHERE mitgliederID = " + mitgliederID + " and lehrgang in (" + build.toString() + ") and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_laufbahn WHERE mitgliederID = " + mitgliederID + " and lehrgang in (" + build.toString() + ") and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getBestandenLehrgangListeID(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by lehrgang;"));
        ResultSet result = statement.executeQuery("SELECT lehrgang FROM mitglieder_laufbahn where mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by lehrgang;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public Vector<Vector<String>> getLaufbahnForTable(int mitgliederID, String kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT l.art, l.datumVon, l.datum, d.beschreibungLang as dienstgradNeu, lk.name as lehrgang, l.lehrgang as lehrgangID, l.UE from mitglieder_laufbahn l LEFT JOIN lehrgang_kategorie lk ON l.lehrgang =  lk.id LEFT JOIN dienstgrad d ON l.neuerDienstgrad = d.id where l.mitgliederID = " + mitgliederID + " and l.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by l.datum ;"));
        ResultSet result = statement.executeQuery("SELECT l.art, l.datumVon, l.datum, d.beschreibungLang as dienstgradNeu, lk.name as lehrgang, l.lehrgang as lehrgangID, l.UE from mitglieder_laufbahn l LEFT JOIN lehrgang_kategorie lk ON l.lehrgang =  lk.id LEFT JOIN dienstgrad d ON l.neuerDienstgrad = d.id where l.mitgliederID = " + mitgliederID + " and l.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by l.datum ;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        if (kategorie == null) {
            kategorie = "NULL";
            logging.logInfo((Object)"Lade LaufbahnDaten mit NULL --> Alle Daten");
        }
        while (result.next()) {
            String art = result.getString("art");
            if (kategorie.equals("Lehrgang")) {
                if (!(art.equals("L") | art.equals("S") | art.equals("E"))) continue;
                liste.add(this.mapResultSetToVectorLehrgang(result));
                continue;
            }
            if (kategorie.equals("F\u00fchrerschein")) {
                if (!art.equals("F\u00fc")) continue;
                liste.add(this.mapResultSetToVectorF\u00fchrerschein(result));
                continue;
            }
            if (kategorie.equals("Funktion")) {
                if (!(art.equals("F") | art.equals("F_Au\u00dferhalb"))) continue;
                liste.add(this.mapResultSetToVectorFunktion(result));
                continue;
            }
            if (kategorie.equals("Ehrungen")) {
                if (!(art.equals("EH") | art.equals("AB"))) continue;
                liste.add(this.mapResultSetToVectorEhrung(result));
                continue;
            }
            if (kategorie.equals("Dienstgrad")) {
                if (!art.equals("D")) continue;
                liste.add(this.mapResultSetToVectorDienstgrad(result));
                continue;
            }
            if (!kategorie.equals("NULL")) continue;
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        if (result.getString("datumVon").toString().equals("")) {
            mitgliederListe.add("-");
        } else {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
        }
        if (result.getString("datum").toString().equals("")) {
            mitgliederListe.add("-");
        } else {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
        }
        String art = result.getString("art");
        if (art.equals("D")) {
            mitgliederListe.add("Bef\u00f6rderung zum  " + result.getString("dienstgradNeu"));
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add("");
        } else if (art.equals("EH")) {
            mitgliederListe.add("");
            mitgliederListe.add("Ehrung:  " + result.getString("lehrgang"));
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add("");
        } else if (art.equals("AB")) {
            mitgliederListe.add("");
            mitgliederListe.add("Abzeichen:  " + result.getString("lehrgang"));
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add("");
        } else if (art.equals("F") | art.equals("F_Au\u00dferhalb")) {
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add("Funktion:  " + result.getString("lehrgang"));
            mitgliederListe.add("");
            mitgliederListe.add("");
        } else {
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add("");
            mitgliederListe.add(result.getString("lehrgang"));
            if (result.getInt("UE") == 0) {
                mitgliederListe.add("n. V.");
            } else {
                mitgliederListe.add(result.getString("UE"));
            }
        }
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorLehrgang(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        String art = result.getString("art");
        if (art.equals("L") | art.equals("S") | art.equals("E")) {
            mitgliederListe.add(result.getString("lehrgang"));
            if (result.getString("datumVon").toString().equals("")) {
                mitgliederListe.add("-");
            } else {
                mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
            }
            if (result.getString("datum").toString().equals("")) {
                mitgliederListe.add("-");
            } else {
                mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
            }
        }
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorF\u00fchrerschein(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        String art = result.getString("art");
        if (art.equals("F\u00fc")) {
            mitgliederListe.add(result.getString("lehrgang"));
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
        }
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorFunktion(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        String art = result.getString("art");
        if (art.equals("F")) {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
            mitgliederListe.add(result.getString("lehrgang"));
            mitgliederListe.add("");
        } else if (art.equals("F_Au\u00dferhalb")) {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
            mitgliederListe.add("");
            mitgliederListe.add(result.getString("lehrgang"));
        }
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorEhrung(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        String art = result.getString("art");
        if (art.equals("EH")) {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
            mitgliederListe.add(result.getString("lehrgang"));
            mitgliederListe.add("");
        } else if (art.equals("AB")) {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
            mitgliederListe.add("");
            mitgliederListe.add(result.getString("lehrgang"));
        }
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorDienstgrad(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        String art = result.getString("art");
        if (art.equals("D")) {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
            mitgliederListe.add(result.getString("dienstgradNeu"));
        }
        return mitgliederListe;
    }
}

