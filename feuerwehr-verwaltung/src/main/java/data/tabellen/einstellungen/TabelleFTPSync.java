/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import go.FTPSync;
import go.FTPSyncDelete;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleFTPSync {
    public void insert(FTPSync sync) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO ftpsync (`id`, `clientID`,`datei`, `ordner`,`status`, `statusDB`,`groesse` , `mandantID`) VALUES ('" + sync.getId() + "', '" + sync.getClientID() + "', '" + sync.getDatei() + "', '" + sync.getOrdner() + "', '" + sync.getStatus() + "', '" + sync.getStausDB() + "', '" + sync.getGroe\u00dfe() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(FTPSync sync) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync set clientID = '" + sync.getClientID() + "', status = " + sync.getStatus() + ", groesse = " + sync.getGroe\u00dfe() + " where datei = '" + sync.getDatei() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateGoesse(long groesse, int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync set groesse = " + groesse + " where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteAll() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from ftpsync where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        String sql2 = "INSERT INTO `ftpsync` (`id`, `clientID`, `datei`, `ordner`, `status`, `statusDB` , `mandantID`) VALUES(1, 'SYSTEM', '', '" + runApplication.arbeitsverzeichnis + "data', 0, 0, " + runApplication.PROPERTIES.get("MandantID") + ");";
        String sql3 = "delete from ftpsync_del where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        String sql4 = "delete from ftpsync_error where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
        logging.logSQL((Object)sql2);
        statement.executeUpdate(sql2);
        logging.logSQL((Object)sql3);
        statement.executeUpdate(sql3);
        logging.logSQL((Object)sql4);
        statement.executeUpdate(sql4);
    }

    public void deleteOneFile(String dateiName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOneFolder(String ordnerName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from ftpsync where ordner = '" + ordnerName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateFTPSync_StatusResert(String dateiName, String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync set status = 0, statusDB = 0 where clientID = '" + clientID + "' and datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateDateiNachUpload(String dateiName, String clientID, long groesse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync set status = 1, groesse = " + groesse + "  where clientID = '" + clientID + "' and datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateDateiNachUpload_STATUSDB(String dateiName, String clientID, long groesse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync set statusDB = 1, groesse = " + groesse + " where clientID = '" + clientID + "' and datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateOrdnerNachUpload(String ordnerName, String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync set status = 1 where clientID = '" + clientID + "' and ordner = '" + ordnerName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getDateiForUpload(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where clientID = '" + clientID + "' and status = 0 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datei FROM ftpsync where clientID = '" + clientID + "' and status = 0 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getDateiForUploadInDB(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT datei FROM ftpsync where clientID = '" + clientID + "' and statusDB = 0 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where clientID = '" + clientID + "' and statusDB = 0 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getIdsForUploadInDB(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM ftpsync where clientID = '" + clientID + "' and statusDB = 0 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM ftpsync where clientID = '" + clientID + "' and statusDB = 0 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getOrdnerForUpload(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ordner FROM ftpsync where clientID = '" + clientID + "' and status = 0  and ordner != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT ordner FROM ftpsync where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllDatei() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where status = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT datei FROM ftpsync where status = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllDateiFromDB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT datei FROM ftpsync where statusDB = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where statusDB = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Long> getAllDateiGroe\u00dfe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT groesse FROM ftpsync where status = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT groesse FROM ftpsync where status = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Long> liste = new ArrayList<Long>();
        while (result.next()) {
            liste.add(result.getLong(1));
        }
        return liste;
    }

    public ArrayList<Long> getAllDateiGroe\u00dfeDB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT groesse FROM ftpsync where statusDB = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT groesse FROM ftpsync where statusDB = 1 and datei != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Long> liste = new ArrayList<Long>();
        while (result.next()) {
            liste.add(result.getLong(1));
        }
        return liste;
    }

    public ArrayList<String> getAllOrdner() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ordner FROM ftpsync where status = 1 and ordner != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT ordner FROM ftpsync where status = 1 and ordner != '' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM ftpsync;");
        logging.logSQL((Object)"SELECT max(id) FROM ftpsync;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getFileID(String dateiName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfFile(String dateiName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfNotUploaded(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM ftpsync where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfNotUploadedDB(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM ftpsync where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getClientIDFromUploader(String dateiName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT clientID FROM ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT clientID FROM ftpsync where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public void insertFTPDelete(FTPSyncDelete sync) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO ftpsync_del (`id`, `clientID`,`datei`,`status`,`statusDB`, `mandantID`) VALUES ('" + sync.getId() + "', '" + sync.getClientID() + "', '" + sync.getDatei() + "', '" + sync.getStatus() + "', '" + sync.getStatusDB() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummerFTPDelete() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM ftpsync_del;");
        logging.logSQL((Object)"SELECT max(id) FROM ftpsync_del;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<String> getDateiForDeleteOnServer(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_del where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datei FROM ftpsync_del where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getDateiForDeleteOnServerDB(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_del where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datei FROM ftpsync_del where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getDateiIDForDeleteOnServerDB(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM ftpsync_del where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM ftpsync_del where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getDateiForDeleteFileSystem() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_del where status = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datei FROM ftpsync_del where status = 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public void updateDateiNachDelete(String dateiName, String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync_del set status = 1 where clientID = '" + clientID + "' and datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateDateiNachDeleteDB(String dateiName, String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ftpsync_del set statusDB = 1 where clientID = '" + clientID + "' and datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCountOfNotDeleted(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync_del where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM ftpsync_del where clientID = '" + clientID + "' and status = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfNotDeletedDB(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync_del where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM ftpsync_del where clientID = '" + clientID + "' and statusDB = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public void insertFTPSync_Error(FTPSync sync) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO ftpsync_error (`clientID`, `datei`, `ordner` , `mandantID`) VALUES ('" + sync.getClientID() + "', '" + sync.getDatei() + "', '" + sync.getOrdner() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void deleteFtpSyncError(String dateiName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from  ftpsync_del where datei = '" + dateiName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getDateiFromErrorList() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_error where ordner = '' and clientID = " + runApplication.clientID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datei FROM ftpsync_error where ordner = '' and clientID = " + runApplication.clientID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getOrdnerFromErrorList() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ordner FROM ftpsync_error where datei = '' and clientID = " + runApplication.clientID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT ordner FROM ftpsync_error where datei = '' and clientID = " + runApplication.clientID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }
}

