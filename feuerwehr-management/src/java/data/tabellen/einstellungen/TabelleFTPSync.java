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
      String sql = "INSERT INTO ftpsync (`id`, `clientID`,`datei`, `ordner`,`status`, `statusDB`,`groesse` , `mandantID`) VALUES (\'" + sync.getId() + "\', \'" + sync.getClientID() + "\', \'" + sync.getDatei() + "\', \'" + sync.getOrdner() + "\', \'" + sync.getStatus() + "\', \'" + sync.getStausDB() + "\', \'" + sync.getGroeße() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(FTPSync sync) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync set clientID = \'" + sync.getClientID() + "\', status = " + sync.getStatus() + ", groesse = " + sync.getGroeße() + " where datei = \'" + sync.getDatei() + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateGoesse(long groesse, int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync set groesse = " + groesse + " where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteAll() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from ftpsync where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      String sql2 = "INSERT INTO `ftpsync` (`id`, `clientID`, `datei`, `ordner`, `status`, `statusDB` , `mandantID`) VALUES(1, \'SYSTEM\', \'\', \'" + runApplication.arbeitsverzeichnis + "data\', 0, 0, " + (String)runApplication.PROPERTIES.get("MandantID") + ");";
      String sql3 = "delete from ftpsync_del where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      String sql4 = "delete from ftpsync_error where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
      logging.logSQL(sql2);
      statement.executeUpdate(sql2);
      logging.logSQL(sql3);
      statement.executeUpdate(sql3);
      logging.logSQL(sql4);
      statement.executeUpdate(sql4);
   }

   public void deleteOneFile(String dateiName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOneFolder(String ordnerName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from ftpsync where ordner = \'" + ordnerName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateFTPSync_StatusResert(String dateiName, String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync set status = 0, statusDB = 0, clientID = \'" + clientID + "\' where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateDateiNachUpload(String dateiName, String clientID, long groesse) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync set status = 1, groesse = " + groesse + "  where clientID = \'" + clientID + "\' and datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateDateiNachUpload_STATUSDB(String dateiName, String clientID, long groesse) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync set statusDB = 1, groesse = " + groesse + " where clientID = \'" + clientID + "\' and datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateOrdnerNachUpload(String ordnerName, String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync set status = 1 where clientID = \'" + clientID + "\' and ordner = \'" + ordnerName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getDateiForUpload(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where clientID = \'" + clientID + "\' and status = 0 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datei FROM ftpsync where clientID = \'" + clientID + "\' and status = 0 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getDateiForUploadInDB(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT datei FROM ftpsync where clientID = \'" + clientID + "\' and statusDB = 0 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where clientID = \'" + clientID + "\' and statusDB = 0 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getIdsForUploadInDB(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM ftpsync where clientID = \'" + clientID + "\' and statusDB = 0 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM ftpsync where clientID = \'" + clientID + "\' and statusDB = 0 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getOrdnerForUpload(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ordner FROM ftpsync where clientID = \'" + clientID + "\' and status = 0  and ordner != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ordner FROM ftpsync where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllDatei() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where status = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT datei FROM ftpsync where status = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllDateiFromDB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT datei FROM ftpsync where statusDB = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync where statusDB = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllDateiGroeße() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT groesse FROM ftpsync where status = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT groesse FROM ftpsync where status = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Long.valueOf(result.getLong(1)));
      }

      return liste;
   }

   public ArrayList getAllDateiGroeßeDB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT groesse FROM ftpsync where statusDB = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT groesse FROM ftpsync where statusDB = 1 and datei != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Long.valueOf(result.getLong(1)));
      }

      return liste;
   }

   public ArrayList getAllOrdner() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ordner FROM ftpsync where status = 1 and ordner != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT ordner FROM ftpsync where status = 1 and ordner != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM ftpsync;");
      logging.logSQL("SELECT max(id) FROM ftpsync;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getFileID(String dateiName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfFile(String dateiName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfNotUploaded(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ftpsync where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfNotUploadedDB(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ftpsync where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getClientIDFromUploader(String dateiName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT clientID FROM ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT clientID FROM ftpsync where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public void insertFTPDelete(FTPSyncDelete sync) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO ftpsync_del (`id`, `clientID`,`datei`,`status`,`statusDB`, `mandantID`) VALUES (\'" + sync.getId() + "\', \'" + sync.getClientID() + "\', \'" + sync.getDatei() + "\', \'" + sync.getStatus() + "\', \'" + sync.getStatusDB() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummerFTPDelete() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM ftpsync_del;");
      logging.logSQL("SELECT max(id) FROM ftpsync_del;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getDateiForDeleteOnServer(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_del where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datei FROM ftpsync_del where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getDateiForDeleteOnServerDB(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_del where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datei FROM ftpsync_del where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getDateiIDForDeleteOnServerDB(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM ftpsync_del where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM ftpsync_del where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getDateiForDeleteFileSystem() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_del where status = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datei FROM ftpsync_del where status = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public void updateDateiNachDelete(String dateiName, String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync_del set status = 1 where clientID = \'" + clientID + "\' and datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateDateiNachDeleteDB(String dateiName, String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ftpsync_del set statusDB = 1 where clientID = \'" + clientID + "\' and datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCountOfNotDeleted(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync_del where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ftpsync_del where clientID = \'" + clientID + "\' and status = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfNotDeletedDB(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ftpsync_del where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ftpsync_del where clientID = \'" + clientID + "\' and statusDB = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public void insertFTPSync_Error(FTPSync sync) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO ftpsync_error (`clientID`, `datei`, `ordner` , `mandantID`) VALUES (\'" + sync.getClientID() + "\', \'" + sync.getDatei() + "\', \'" + sync.getOrdner() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteFtpSyncError(String dateiName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from  ftpsync_del where datei = \'" + dateiName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getDateiFromErrorList() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datei FROM ftpsync_error where ordner = \'\' and clientID = " + runApplication.clientID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datei FROM ftpsync_error where ordner = \'\' and clientID = " + runApplication.clientID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getOrdnerFromErrorList() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ordner FROM ftpsync_error where datei = \'\' and clientID = " + runApplication.clientID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ordner FROM ftpsync_error where datei = \'\' and clientID = " + runApplication.clientID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
