/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriffBLOB;
import go.Dateisystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleDateisystem {
    public void insert(Dateisystem dateisystem) throws SQLException, FileNotFoundException {
        Statement statement = DatenbankZugriffBLOB.getInstance().getDbConnection().createStatement();
        String sql = "delete from dateisystem where id = " + dateisystem.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
        PreparedStatement ps = DatenbankZugriffBLOB.getInstance().getDbConnection().prepareStatement("INSERT INTO dateisystem (`id`,`dateiStream`,`mandantID`) VALUES (?, ?, ?);");
        logging.logSQL((Object)"INSERT INTO dateisystem (`id`,`jahr`, `datei` ,`groesse` ,`dateiStream`,`mandantID`) VALUES (?, ?, ?);");
        logging.logInfo((Object)("Daei: " + dateisystem.getDatei().getAbsolutePath()));
        FileInputStream fis = new FileInputStream(dateisystem.getDatei());
        ps.setInt(1, dateisystem.getId());
        ps.setBinaryStream(2, (InputStream)fis, (int)dateisystem.getDatei().length());
        ps.setString(3, runApplication.PROPERTIES.get("MandantID"));
        ps.executeUpdate();
    }

    public void read(String dateiName) throws Exception {
        long zeit1 = System.currentTimeMillis();
        PreparedStatement ps = DatenbankZugriffBLOB.getInstance().getDbConnection().prepareStatement("Select f.datei, d.dateiStream, f.groesse from dateisystem d LEFT JOIN ftpsync f ON d.id = f.id where f.datei = '" + dateiName + "' and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("Select f.datei, d.dateiStream, f.groesse from dateisystem d LEFT JOIN ftpsync f ON d.id = f.id where f.datei = '" + dateiName + "' and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            File file = new File(resultSet.getString(1));
            new File(file.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(resultSet.getString(1));
            byte[] buffer = new byte[(int)resultSet.getLong(3)];
            InputStream is = resultSet.getBinaryStream(2);
            while (is.read(buffer) > 0) {
                fos.write(buffer);
            }
            fos.close();
        }
        long zeit2 = System.currentTimeMillis();
        long summe = zeit2 - zeit1;
        logging.logSQL((Object)("Extrahierungszeit aus DB: " + summe + " ms"));
    }

    public void mysqlEinstellungenSetzen() throws SQLException {
        Statement statement = DatenbankZugriffBLOB.getInstance().getDbConnection().createStatement();
        String sql = "SET GLOBAL max_allowed_packet = 1024*1024*14;";
        statement.executeUpdate(sql);
    }

    public void deleteAll() throws SQLException {
        Statement statement = DatenbankZugriffBLOB.getInstance().getDbConnection().createStatement();
        String sql = "delete from dateisystem where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(int id) throws SQLException {
        Statement statement = DatenbankZugriffBLOB.getInstance().getDbConnection().createStatement();
        String sql = "delete from dateisystem where id = " + id + " and  mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }
}

