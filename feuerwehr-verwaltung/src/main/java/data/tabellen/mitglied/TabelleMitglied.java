/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleMitglied {
    public void insert(Mitglieder mitglied) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder (`id`, `mitgliederGruppe`,`anrede`,`name`, `vorname`, `strasse`, `ort`, `telefonPrivat`, `telefonMobil`, `telefonArbeit`, `telegrammID`, `email`, `email2`, `beruf`, `dienstgrad`, `ausserDienst`, `mitgliedSeit`, `mitgliedBis`, `gebDatum`, `hochzeit`, `kommentar`,`fuehrerscheinNummer`,`fahrberechtigungNummer`,`dienstausweisNummer`, `loeschkenner`, `eMailDeaktiv`, `mandantID`) VALUES ('" + mitglied.getId() + "', '" + mitglied.getMitgliederGruppe() + "', '" + mitglied.getAnrede() + "', '" + mitglied.getName() + "', '" + mitglied.getVorname() + "', '" + mitglied.getStrasse() + "', '" + mitglied.getOrt() + "', '" + mitglied.getTelefonePrivate() + "', '" + mitglied.getTelefonMobile() + "', '" + mitglied.getTelefonArbeit() + "', '" + mitglied.getTelegrammID() + "', '" + mitglied.getEmail() + "', '" + mitglied.getEmail2() + "', '" + mitglied.getBeruf() + "', '" + mitglied.getDienstgrad() + "', '" + mitglied.getAusserDienst() + "', '" + mitglied.getMitgliedSeit() + "', '" + mitglied.getMitgliedBis() + "', '" + mitglied.getGebDatum() + "', '" + mitglied.getHochzeit() + "', '" + mitglied.getKommentar() + "', '" + mitglied.getFuehrerscheinNummer() + "', '" + mitglied.getFahrberechtigungNummer() + "', '" + mitglied.getDienstausweisNummer() + "', '" + mitglied.getLoschkenner() + "', '" + mitglied.geteMailVerteilung() + "', '" + mitglied.getMandantID() + "');";
        statement.executeUpdate(sql);
    }

    public void update(Mitglieder mitglied) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder set mitgliederGruppe = '" + mitglied.getMitgliederGruppe() + "', anrede = '" + mitglied.getAnrede() + "', name = '" + mitglied.getName() + "', vorname = '" + mitglied.getVorname() + "', strasse = '" + mitglied.getStrasse() + "', ort = '" + mitglied.getOrt() + "', telefonPrivat = '" + mitglied.getTelefonePrivate() + "', telefonMobil = '" + mitglied.getTelefonMobile() + "', telefonArbeit = '" + mitglied.getTelefonArbeit() + "', telegrammID = '" + mitglied.getTelegrammID() + "', email = '" + mitglied.getEmail() + "', email2 = '" + mitglied.getEmail2() + "', beruf = '" + mitglied.getBeruf() + "', dienstgrad = '" + mitglied.getDienstgrad() + "', ausserDienst = '" + mitglied.getAusserDienst() + "', mitgliedSeit = '" + mitglied.getMitgliedSeit() + "', mitgliedBis = '" + mitglied.getMitgliedBis() + "', gebDatum = '" + mitglied.getGebDatum() + "', hochzeit = '" + mitglied.getHochzeit() + "', kommentar = '" + mitglied.getKommentar() + "', fuehrerscheinNummer = '" + mitglied.getFuehrerscheinNummer() + "', fahrberechtigungNummer = '" + mitglied.getFahrberechtigungNummer() + "', dienstausweisNummer = '" + mitglied.getDienstausweisNummer() + "', loeschkenner = '" + mitglied.getLoschkenner() + "', eMailDeaktiv = '" + mitglied.geteMailVerteilung() + "' where id = " + mitglied.getId() + " and mandantID = " + mitglied.getMandantID() + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateAusserDienst(int id, int ausserDienstStatus) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder set ausserDienst = " + ausserDienstStatus + " where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateLoeschkenner(int id, int loeschkennerstatus) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder set loeschkenner = " + loeschkennerstatus + " where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextFreeNumber() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT (Id+1) AS FreieId FROM mitglieder WHERE (Id + 1) NOT IN (SELECT Id FROM mitglieder) ORDER BY FreieId;");
        ResultSet result = statement.executeQuery("SELECT (Id+1) AS FreieId FROM mitglieder WHERE (Id + 1) NOT IN (SELECT Id FROM mitglieder) ORDER BY FreieId;");
        if (result.next()) {
            if (result.getInt(1) < 11000) {
                return 11000;
            }
            return result.getInt(1);
        }
        return 11000;
    }

    public int getMitglierderGruppenCount(int mitgliederGruppenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `mitglieder` WHERE mitgliederGruppe = " + mitgliederGruppenID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `mitglieder` WHERE mitgliederGruppe = " + mitgliederGruppenID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getDienstgradCount(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getDienstgradCountMitgliedergruppe1(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMitgliederCountGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMitgliederCountGruppe1OhneGeburtstag() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and gebDatum != '' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and gebDatum != ''  and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMitgliederCountAGTGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAllMitgliederCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMitgliederCountByNachnameVorname(String name, String vorname) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where name = '" + name + "' and vorname = '" + vorname + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder where name = '" + name + "' and vorname = '" + vorname + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getMitgliederGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname ;");
        logging.logSQL((Object)("SELECT name, vorname FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<Integer> getMitgliederIDGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname ;");
        logging.logSQL((Object)("SELECT id FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleTruppUndGruppenfuehrerDerGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (15,19) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname");
        logging.logSQL((Object)("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (15,19) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getAlleMitF\u00fchrerscheinDerGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (1,2,3) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname");
        logging.logSQL((Object)("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (1,2,3) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getAlleAtemschutztraeger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
        logging.logSQL((Object)("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getMitgliederEinerGruppe(int mitgliederGruppe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id, name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT id, name, vorname FROM mitglieder where and mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("(" + result.getString(1) + ") " + result.getString(2) + ", " + result.getString(3));
        }
        return liste;
    }

    public ArrayList<String> getMitgliederEinerGruppeForSearch(int mitgliederGruppe, String suche) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id, name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and (name like '%" + suche + "%' or vorname like '%" + suche + "%') order by name, vorname;");
        logging.logSQL((Object)("SELECT id, name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and (name like '%" + suche + "%' or vorname like '%" + suche + "%') order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("(" + result.getString(1) + ") " + result.getString(2) + ", " + result.getString(3));
        }
        return liste;
    }

    public ArrayList<String> getAllMitgliederFromDataBase() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname ;");
        logging.logSQL((Object)("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<Integer> getAllMitgliederNummernFromDataBase() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname ;");
        logging.logSQL((Object)("SELECT id FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllMitgliederGruppenFromDataBaseByMitglied() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT mitgliederGruppe FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname ;");
        logging.logSQL((Object)("SELECT mitgliederGruppe FROM mitglieder where loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAllMitgliederFromDataBaseWithEMail(String spalte) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and " + spalte + " != '' order by mitgliederGruppe, name, vorname;"));
        ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and " + spalte + " != '' order by mitgliederGruppe, name, vorname ;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public Vector<Vector<String>> getAllMitgliederForTable(int mitgliederGruppenID, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.email, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.email, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getAllMitgliederForTableUebersicht2(int mitgliederGruppenID, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonArbeit, m.email2, m.mitgliedSeit, m.kommentar FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonArbeit, m.email2, m.mitgliedSeit, m.kommentar FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVectorUebersicht2(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getAllMitgliederForTableTelefonliste(int mitgliederGruppenID, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonPrivat, m.telefonMobil, m.telefonArbeit FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonPrivat, m.telefonMobil, m.telefonArbeit FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVectorTelefonliste(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getAllMitgliederForTableZusatzdaten(int mitgliederGruppenID, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.fuehrerscheinNummer, u.ablaufLKW, m.dienstausweisNummer, u.ablaufDienstausweis, m.fahrberechtigungNummer, u. pruefungDerFahrberechtigung FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_untersuchung u ON m.id = u.id where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.fuehrerscheinNummer,  u.ablaufLKW, m.dienstausweisNummer, u.ablaufDienstausweis, m.fahrberechtigungNummer, u. pruefungDerFahrberechtigung FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_untersuchung u ON m.id = u.id where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVectorZusatzdaten(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getAllGeburtstageForTable(int mitgliederGruppe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String additionalFilter = mitgliederGruppe == 0 ? "" : "and m.mitgliederGruppe = " + mitgliederGruppe;
        logging.logSQL((Object)("SELECT mg.name as mitgliederGruppe, d.beschreibung as dienstgrad, m.name, m.vorname, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe mg ON mg.id = m.mitgliederGruppe where m.ausserDienst = 0  and m.loeschkenner = 0  " + additionalFilter + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and mg.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and m.gebDatum != '' order by SUBSTRING(m.gebDatum,6,10), m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT mg.name as mitgliederGruppe, d.beschreibung as dienstgrad, m.name, m.vorname, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe mg ON mg.id = m.mitgliederGruppe where m.ausserDienst = 0  and m.loeschkenner = 0 " + additionalFilter + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and mg.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and m.gebDatum != '' order by SUBSTRING(m.gebDatum,6,10), m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToGeburtstagVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<String> getEinheitsf\u00fchrerMail() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 26 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 26 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getString(1).equals("")) continue;
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getGeraetewarteMail() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 27 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 27 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getString(1).equals("")) continue;
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleMailAdressenGruppe1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT email FROM mitglieder where mitgliederGruppe = 1 and loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT email FROM mitglieder where mitgliederGruppe = 1 and loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getString(1).equals("")) continue;
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getVCardExport(int mitgliederGruppe, String seperator) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.vorname, m.name, a.name, m.gebDatum, m.kommentar, m.email, m.email2, m.telefonPrivat, m.telefonMobil, m.strasse, m.ort, m.telefonArbeit, g.name FROM mitglieder m LEFT JOIN mitglieder_anrede a ON m.anrede = a.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id where m.mitgliederGruppe = " + mitgliederGruppe + " and loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and g.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        logging.logSQL((Object)("SELECT m.vorname, m.name, a.name, m.gebDatum, m.kommentar, m.email, m.email2, m.telefonPrivat, m.telefonMobil, m.strasse, m.ort, m.telefonArbeit, g.name FROM mitglieder m LEFT JOIN mitglieder_anrede a ON m.anrede = a.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id where m.mitgliederGruppe = " + mitgliederGruppe + " and loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and g.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + seperator + seperator + result.getString(2) + seperator + result.getString(3) + seperator + seperator + seperator + seperator + seperator + result.getString(4) + seperator + seperator + seperator + seperator + seperator + result.getString(5) + seperator + result.getString(6) + seperator + result.getString(7) + seperator + seperator + seperator + result.getString(8) + seperator + seperator + result.getString(9) + seperator + seperator + seperator + seperator + result.getString(10) + seperator + seperator + seperator + seperator + result.getString(11) + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + result.getString(12) + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + result.getString(13));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareMaschinistenKlasseC(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseC = 1 and m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareMaschinistenKlasseCuB(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseB = 1 or l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseB = 1 or l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareTruppfuehrer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.TF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.TF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareGruppenfuehrer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.gf = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.gf = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareZugfuehrer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.gf = 1 or l.zf = 1) and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.gf = 1 or l.zf = 1) and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareAtemschtztraeger(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.agt = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.agt = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareMannschaft(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.tm1 = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.tm1 = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareRettunsdienstPersonal(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and m.mitgliederGruppe = 1 and (l.rh = 1 or l.rs = 1 or l.ra = 1) order by m.name, m.vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and m.mitgliederGruppe = 1 and (l.rh = 1 or l.rs = 1 or l.ra = 1) order by m.name, m.vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareRTWFahrerKlasseC(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareRTWFahrerKlasseB(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseB = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseB = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareDLKFahrer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.dlkmaschi = 1 or l.korbsteuerung = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.dlkmaschi = 1 or l.korbsteuerung = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getVerfuegbareDLKGF(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.dlkmaschi = 1 and l.GF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.dlkmaschi = 1 and l.GF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public HashMap<String, String> getAllMitgliederData(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.id, g.name as mitgliederGruppe, a.name as anrede, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.telefonArbeit, m.telegrammID, m.email, m.email2, m.beruf, m.gebDatum, m.hochzeit, d.beschreibungLang as dienstgrad, m.ausserDienst, m.mitgliedSeit, m.mitgliedBis, m.kommentar, m.fuehrerscheinNummer, m.fahrberechtigungNummer, m.dienstausweisNummer, m.eMailDeaktiv  FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id LEFT JOIN mitglieder_anrede a ON m.anrede = a.id where m.id = " + mitgliedID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT m.id, g.name as mitgliederGruppe, a.name as anrede, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.telefonArbeit, m.telegrammID, m.email, m.email2, m.beruf, m.gebDatum, m.hochzeit, d.beschreibungLang as dienstgrad, m.ausserDienst, m.mitgliedSeit, m.mitgliedBis, m.kommentar, m.fuehrerscheinNummer, m.fahrberechtigungNummer, m.dienstausweisNummer, m.eMailDeaktiv  FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id LEFT JOIN mitglieder_anrede a ON m.anrede = a.id where m.id = " + mitgliedID + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("id", Integer.toString(result.getInt("id")));
            map.put("mitgliederGruppe", result.getString("mitgliederGruppe"));
            map.put("anrede", result.getString("anrede"));
            map.put("name", result.getString("name"));
            map.put("vorname", result.getString("vorname"));
            map.put("strasse", result.getString("strasse"));
            map.put("ort", result.getString("ort"));
            map.put("telefonPrivat", result.getString("telefonPrivat"));
            map.put("telefonMobil", result.getString("telefonMobil"));
            map.put("telefonArbeit", result.getString("telefonArbeit"));
            map.put("telegrammID", result.getString("telegrammID"));
            map.put("email", result.getString("email"));
            map.put("email2", result.getString("email2"));
            map.put("beruf", result.getString("beruf"));
            map.put("geburtsdatum", result.getString("gebDatum"));
            map.put("hochzeit", result.getString("hochzeit"));
            map.put("dienstgrad", result.getString("dienstgrad"));
            map.put("ausserDienst", Integer.toString(result.getInt("ausserDienst")));
            map.put("mitgliedSeit", result.getString("mitgliedSeit"));
            map.put("mitgliedBis", result.getString("mitgliedBis"));
            map.put("kommentar", result.getString("kommentar"));
            map.put("fuehrerscheinNummer", result.getString("fuehrerscheinNummer"));
            map.put("fahrberechtigungNummer", result.getString("fahrberechtigungNummer"));
            map.put("dienstausweisNummer", result.getString("dienstausweisNummer"));
            map.put("eMailDeaktiv", result.getString("eMailDeaktiv"));
        }
        return map;
    }

    public HashMap<Integer, String> getMitgliederListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id, name, vorname FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id, name, vorname FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        while (result.next()) {
            map.put(result.getInt(1), String.valueOf(result.getString(2)) + ", " + result.getString(3));
        }
        return map;
    }

    public int getId(String name, String vorname) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where name = '" + name + "' and vorname = '" + vorname + "';");
        logging.logSQL((Object)("SELECT id FROM mitglieder where name = '" + name + "' and vorname = '" + vorname + "';"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getIdByGuiString(String guiString) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        int komma = guiString.indexOf(",");
        String isSelectedName = guiString.substring(0, komma);
        String isSelectedVorname = guiString.substring(komma + 2, guiString.length());
        ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where name = '" + isSelectedName + "' and vorname = '" + isSelectedVorname + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM mitglieder where name = '" + isSelectedName + "' and vorname = '" + isSelectedVorname + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getEinsatzleiter(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON d.id = m.dienstgrad where m.id = " + id + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON d.id = m.dienstgrad where m.id = " + id + " and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtEinsatzleiterMitDienstgrad").equals("1")) {
                return String.valueOf(result.getString(1)) + " " + result.getString(2) + ", " + result.getString(3);
            }
            return String.valueOf(result.getString(2)) + ", " + result.getString(3);
        }
        return null;
    }

    public int getAnrede(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT anrede FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT anrede FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getName(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getNameVornameByID(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name, vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return String.valueOf(result.getString(1)) + ", " + result.getString(2);
        }
        return null;
    }

    public String getVorname(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getStrasse(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT strasse FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT strasse FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getOrt(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ort FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT ort FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelefonPrivat(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT telefonPrivat FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT telefonPrivat FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelefonMobil(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT telefonMobil FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT telefonMobil FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelefonArbeit(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT telefonArbeit FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT telefonArbeit FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelegrammID(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT telegrammID FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT telegrammID FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEMail(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT email FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT email FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAlleEMailEinerMitgliederGruppe(int mitgliederGruppe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT email FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + " and email != '' and eMailDeaktiv = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT email FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + " and email != '' and eMailDeaktiv = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public String getEMail2(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT email2 FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT email2 FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getGebDatum(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT gebDatum FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT gebDatum FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getBeruf(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT beruf FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT beruf FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getSummiertesGebJahr() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(SUBSTRING(gebDatum,1,4)) FROM mitglieder where mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(SUBSTRING(gebDatum,1,4)) FROM mitglieder where mitgliederGruppe = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getGebDatumForInformationService(int modus) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = null;
        if (modus == 1) {
            logging.logSQL((Object)("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != '' and gebdatum like '%" + SbcUtils.timeStamp((String)"MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname"));
            result = statement.executeQuery("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != '' and gebdatum like '%" + SbcUtils.timeStamp((String)"MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name, vorname");
        } else if (modus == 2) {
            logging.logSQL((Object)("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != '' and gebdatum like '%-" + SbcUtils.timeStamp((String)"MM") + "-%' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by SUBSTRING(gebDatum,6,8), name, vorname"));
            result = statement.executeQuery("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != '' and gebdatum like '%-" + SbcUtils.timeStamp((String)"MM") + "-%' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by SUBSTRING(gebDatum,6,8), name, vorname");
        }
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2) + " " + TimeCalculation.parseDateForGUI(result.getString(3)));
        }
        return liste;
    }

    public String getDienstgradLangText(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.beschreibungLang FROM mitglieder m LEFT JOIN dienstgrad d on d.id= m.dienstgrad where m.id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT d.beschreibungLang FROM mitglieder m LEFT JOIN dienstgrad d on d.id= m.dienstgrad where m.id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getDienstgradID(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dienstgrad from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dienstgrad from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getMitgliedSeit(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT mitgliedSeit from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT mitgliedSeit from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            System.out.println("Mitglied seit (Jahr)--> " + result.getString(1).toString().substring(0, 4));
            return result.getString(1).toString().substring(0, 4);
        }
        return null;
    }

    public String getMitgliedKommentar(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT kommentar from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT kommentar from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getMitgliederGruppe(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT mitgliederGruppe from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT mitgliederGruppe from mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAusserDienstStatus(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ausserDienst FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT ausserDienst FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getEMailDeaktivStatus(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT eMailDeaktiv FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT eMailDeaktiv FROM mitglieder where id = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("dienstgrad"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add(result.getString("strasse"));
        mitgliederListe.add(result.getString("ort"));
        mitgliederListe.add(result.getString("telefonPrivat"));
        mitgliederListe.add(result.getString("telefonMobil"));
        mitgliederListe.add(result.getString("email"));
        mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("gebDatum")));
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorUebersicht2(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("dienstgrad"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add(result.getString("telefonArbeit"));
        mitgliederListe.add(result.getString("email2"));
        if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy")) {
            mitgliederListe.add(result.getString("mitgliedSeit").substring(0, 4));
        } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
            if (result.getString("mitgliedSeit").toString().length() == 4) {
                mitgliederListe.add(TimeCalculation.parseDateForGUI(String.valueOf(result.getString("mitgliedSeit")) + "-01-01"));
            } else {
                mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("mitgliedSeit")));
            }
        }
        mitgliederListe.add(result.getString("kommentar"));
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorTelefonliste(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("dienstgrad"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add(result.getString("telefonPrivat"));
        mitgliederListe.add(result.getString("telefonMobil"));
        mitgliederListe.add(result.getString("telefonArbeit"));
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToVectorZusatzdaten(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("dienstgrad"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add(result.getString("fuehrerscheinNummer"));
        mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("ablaufLKW")));
        mitgliederListe.add(result.getString("dienstausweisNummer"));
        mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("ablaufDienstausweis")));
        mitgliederListe.add(result.getString("fahrberechtigungNummer"));
        mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("pruefungDerFahrberechtigung")));
        return mitgliederListe;
    }

    private Vector<String> mapResultSetToGeburtstagVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("mitgliederGruppe"));
        if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
            mitgliederListe.add(result.getString("dienstgrad"));
        }
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("gebDatum")));
        return mitgliederListe;
    }
}

