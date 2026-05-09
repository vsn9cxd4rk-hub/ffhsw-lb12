/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrzeugeinteilung_temp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleFahrzeugeinteilung_temp {
    public void insert(Fahrzeugeinteilung_temp temp) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO fahrzeugeinteilung_temp (`mitgliederID`, `dienstgradID`, `klasseC`, `klasseB`, `Maschi`,`dlkmaschi`,`korbsteuerung`, `chef`, `tm1`, `AGT`,`TF`,`GF`,`ZF`,`rh`,`rs`,`ra`, `beteiligung`, `position`, `mandantID`) VALUES ('" + temp.getMitgliederID() + "', '" + temp.getDienstgradID() + "', '" + temp.getKlasseC() + "', '" + temp.getKlasseB() + "', '" + temp.getMaschi() + "', '" + temp.getDlkmaschi() + "', '" + temp.getKorbsteuerung() + "', '" + temp.getChef() + "', '" + temp.getTm1() + "', '" + temp.getAgt() + "', '" + temp.getTf() + "', '" + temp.getGf() + "', '" + temp.getZf() + "', '" + temp.getRh() + "', '" + temp.getRs() + "', '" + temp.getRa() + "', '" + temp.getBeteiligung() + "', '" + temp.getPosition() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void updatePosition(int mitgliederID, int count) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrzeugeinteilung_temp set position = " + count + " where mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteAll() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from fahrzeugeinteilung_temp where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(String mitgliederNameAusComboBox) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        if (!mitgliederNameAusComboBox.equals("<bitte w\u00e4hlen>")) {
            logging.logInfo((Object)("--> L\u00f6sche Daten von Temp Tabelle: " + mitgliederNameAusComboBox));
            TabelleMitglied tabMitglieder = new TabelleMitglied();
            String sql = "delete from fahrzeugeinteilung_temp where mitgliederID = " + tabMitglieder.getIdByGuiString(mitgliederNameAusComboBox) + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
            logging.logSQL((Object)sql);
            statement.executeUpdate(sql);
        }
    }

    public int getCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung_temp where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getRestOfMitglieder() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM fahrzeugeinteilung_temp ft LEFT JOIN mitglieder m ON ft.mitgliederID = m.id where ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM fahrzeugeinteilung_temp ft LEFT JOIN mitglieder m ON ft.mitgliederID = m.id where ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public int getGruppenfuehrerCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE gf = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE gf = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountErfahrenstenChef() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.beteiligung desc;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getErfahrenstenChef() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenGruppenfuehrer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.gf = 1 and ft.chef = 0 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung asc"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.gf = 1 and ft.chef = 0 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung asc");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public int getMaschiCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE maschi = 1 and klasseC = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE maschi = 1 and klasseC = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getErfahrenstenMaschnistKlasseC() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseC = 1 and ft.maschi = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getUnerfahrenstenMaschnistKlasseC() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseC = 1 and ft.maschi = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenMaschnistKlasseB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseB = 1 and ft.maschi = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getMaschiOhneTruppf\u00fchrer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseC = 1 and ft.maschi = 1 and ft.tf = 0 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public int getTfCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE tf = 1 and gf = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE tf = 1 and gf = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getErfahrenstenAngriffstruppf\u00fchrer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.TF = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenAngriffstruppf\u00fchrerMitKlasseB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.TF = 1 and ft.klasseB = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenAngriffstruppf\u00fchrerMitKlasseC() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.TF = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getUnerfahrenstenAngriffstruppmann() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.AGT = 1 and ft.TF = 0 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung asc";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getAgtTr\u00e4ger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.AGT = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenerenAngriffstruppmann() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.AGT = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.dienstgradID";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getMelder() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID where ft.tm1 = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenRA() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.ra = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.ra = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenRS() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenRSMitKlasseC() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenRSMitKlasseB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseB = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseB = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public int getRsCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE rs = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE rs = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getErfahrenstenRH() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenRHMitKlasseC() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenRHMitKlasseB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseB = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseB = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenDLKGF() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.GF = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.GF = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenDLKFahrer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.klasseC = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenTFMitKorbEinweisung() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tf = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tf = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenFMMitKorbEinweisung() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tm1 = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tm1 = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }

    public String getErfahrenstenZF() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.zf = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.zf = 1 and ft.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
        if (result.next()) {
            logging.logInfo((Object)("Auswahl: " + result.getString(1) + ", " + result.getString(2)));
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return "<bitte w\u00e4hlen>";
    }
}

