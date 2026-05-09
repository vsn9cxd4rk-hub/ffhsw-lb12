/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Briefe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBriefe {
    public void insert(Briefe brief) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO briefe (`id`,`jahr`, `title`, `bericht`, `erstelldatum`, `dateiname`, `empfaenger`, `template`, `mandantID`) VALUES ('" + brief.getId() + "', '" + brief.getJahr() + "', '" + brief.getTitle() + "', '" + brief.getBericht() + "', '" + brief.getErstelldatum() + "', '" + brief.getDateiname() + "', '" + brief.getEmpfaenger() + "', '" + brief.getTemplate() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void updateTemplate(String templateName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update briefe set template = 0 where title = '" + templateName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM briefe;");
        logging.logSQL((Object)"SELECT max(id) FROM briefe;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getBrief(String title) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dateiname FROM briefe where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dateiname FROM briefe where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getText(String title) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT bericht FROM briefe where title = '" + title + "' and template = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT bericht FROM briefe where title = '" + title + "' and template = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getTemplates() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT title FROM briefe where template = 1  and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title, jahr;");
        logging.logSQL((Object)("SELECT title FROM briefe where template = 1  and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title, jahr;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }
}

