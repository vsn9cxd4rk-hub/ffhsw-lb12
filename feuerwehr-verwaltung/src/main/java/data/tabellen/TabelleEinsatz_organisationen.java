/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Einsatz_organisationen;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class TabelleEinsatz_organisationen {
    public void insertArray(Einsatz_organisationen[] organisation) throws SQLException {
        StringBuilder buildStatement = new StringBuilder();
        buildStatement.append("INSERT INTO einsatz_organisationen (`id`, `veranstaltungID` , `organisationID`, `status`, `mandantID`) VALUES ");
        int i = 0;
        while (i < organisation.length) {
            buildStatement.append("('");
            buildStatement.append(organisation[i].getId());
            buildStatement.append("', '");
            buildStatement.append(organisation[i].getVeranstaltungID());
            buildStatement.append("', '");
            buildStatement.append(organisation[i].getOrganisationID());
            buildStatement.append("', '");
            buildStatement.append(organisation[i].getStatus());
            buildStatement.append("', '");
            buildStatement.append(runApplication.PROPERTIES.get("MandantID"));
            if (i != organisation.length - 1) {
                buildStatement.append("'),");
            } else {
                buildStatement.append("');");
            }
            ++i;
        }
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)buildStatement.toString());
        statement.executeUpdate(buildStatement.toString());
    }

    public void insert(Einsatz_organisationen organisation) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO einsatz_organisationen (`id`, `veranstaltungID` , `organisationID`, `status`, `mandantID`) VALUES ('" + organisation.getId() + "', '" + organisation.getVeranstaltungID() + "', '" + organisation.getOrganisationID() + "', '" + organisation.getStatus() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from einsatz_organisationen where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM einsatz_organisationen;");
        logging.logSQL((Object)"SELECT max(id) FROM einsatz_organisationen;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCount(int organisationID, int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_organisationen where organisationID = " + organisationID + " and status = " + status + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM einsatz_organisationen where organisationID = " + organisationID + " and status = " + status + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public boolean getStatusOfOrganisation(int veranstaltungID, int organisationID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT status FROM einsatz_organisationen where veranstaltungID = " + veranstaltungID + " and organisationID = " + organisationID + "  and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT status FROM einsatz_organisationen where veranstaltungID = " + veranstaltungID + " and organisationID = " + organisationID + "  and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1) != 0;
        }
        return false;
    }

    public String getOrganisationIDKommaSeperated(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT eo.organisationID FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;"));
        ResultSet result = statement.executeQuery("SELECT eo.organisationID FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        String[] idListe = Utils.listToArray(liste);
        StringBuilder build = new StringBuilder();
        if (idListe.length != 0) {
            int i = 0;
            while (i < idListe.length) {
                build.append(idListe[i]);
                if (i != idListe.length - 1) {
                    build.append(",");
                }
                ++i;
            }
        } else {
            build.append("1");
        }
        logging.logSQL((Object)("getOrganisationIDKommaSeperated = " + build.toString()));
        return build.toString();
    }

    public String getOrganisationNameKommaSeperated(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;"));
        ResultSet result = statement.executeQuery("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        String[] idListe = Utils.listToArray(liste);
        StringBuilder build = new StringBuilder();
        if (idListe.length != 0) {
            int i = 0;
            while (i < idListe.length) {
                if (idListe[i].equals("") | idListe[i] == null) {
                    build.append(runApplication.EINSTELLUNGEN.get("Name"));
                } else {
                    build.append(idListe[i]);
                }
                if (i != idListe.length - 1) {
                    build.append(", ");
                }
                ++i;
            }
        } else {
            build.append(runApplication.EINSTELLUNGEN.get("Name"));
        }
        logging.logSQL((Object)("getOrganisationIDKommaSeperated = " + build.toString()));
        return build.toString();
    }

    public Vector<Vector<String>> getAnwesendeOrganisationen(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.organisationID > 1 and  eo.status = 1 and eo.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.organisationID > 1 and  eo.status = 1 and eo.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> anwesenheitListe = new Vector<String>();
        anwesenheitListe.add(result.getString("name"));
        return anwesenheitListe;
    }
}

