/*
 * Decompiled with CFR 0.152.
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.MitgliederAkte_Kommentar;
import java.sql.SQLException;
import java.sql.Statement;
import run.runApplication;

public class TabelleMitgliederakte_kommentar {
    public void insert(MitgliederAkte_Kommentar kommentar) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitgliederakte_kommentar (`id`, `datum`,`zeit`,`kommentar`, `mandantID`) VALUES ('" + kommentar.getId() + "', '" + kommentar.getDatum() + "', '" + kommentar.getZeit() + "', '" + kommentar.getKommentar() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }
}

