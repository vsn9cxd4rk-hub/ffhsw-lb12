package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import go.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import logging.logging;
import run.runApplication;
import utilities.hash;

public class TabelleUser {

   public List getAll() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("Select count(*) from User where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("Select count(*) from User where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(this.mapResultSetToUser(result));
      }

      return liste;
   }

   public int getRechte(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("Select admin from user where userid =\'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("Select admin from user where userid =\'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getUserGruppe(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("Select usergruppe from user where userid =\'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("Select usergruppe from user where userid =\'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public User get(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT * FROM user WHERE UserId = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      ResultSet result = statement.executeQuery(sql);
      return result.next()?this.mapResultSetToUser(result):null;
   }

   public void insert(User user) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO `user`(`UserId`, `Passwort`, `usergruppe`, `Admin`, `deaktiv`, `loeschkenner` , `mandantID`) VALUES (\'" + user.getUser() + "\',\'" + user.getPasswort() + "\',\'" + user.getUsergruppe() + "\',\'" + user.getAdmin() + "\',\'" + user.getDeaktiv() + "\',\'" + user.getLoeschkenner() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void updatepasswort(String id, String passwort) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "UPDATE `user`SET passwort = \'" + passwort + "\' where userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void updateDeaktiv(String id, int status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "UPDATE `user` SET deaktiv = \'" + status + "\' where userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void updateLoeschkenner(String id, int status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "UPDATE `user`SET loeschkenner = " + status + " where userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void updateBerechtigungsProfil(String id, int berechtigung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "UPDATE `user`SET admin = " + berechtigung + " where userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void updateAdministratorRecht(String id, String recht) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "UPDATE `user`SET usergruppe = \'" + recht + "\' where userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public int getBenutzerExist(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT count(*) FROM user WHERE UserId = \'" + id + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getInt(1):0;
   }

   public int getDeaktivStatus(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT deaktiv FROM user WHERE userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getInt(1):0;
   }

   public String getAdministratorStatus(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT usergruppe FROM user WHERE userid = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getString(1):null;
   }

   public int getAltesPasswort(String id, String passwort) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT count(*) FROM user WHERE UserId = \'" + id + "\' and passwort = \'" + passwort + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getInt(1):0;
   }

   public String getPasswort(String id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT passwort FROM user WHERE UserId = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getString(1):null;
   }

   public String getPasswortForMASTERUSER() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT passwort FROM user WHERE UserId = \'admin\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      ResultSet result = statement.executeQuery(sql);
      return result.next()?hash.decodeHashCode(result.getString(1)):null;
   }

   public ArrayList getUserListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT userid from user where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID"));
      logging.logSQL("SELECT userid from user where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID"));
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   private User mapResultSetToUser(ResultSet result) throws SQLException {
      User user = new User();
      user.setUser(result.getString("UserId"));
      user.setPasswort(result.getString("Passwort"));
      user.setUsergruppe(result.getString("UserGruppe"));
      user.setAdmin(result.getInt("Admin"));
      user.setDeaktiv(result.getInt("Deaktiv"));
      return user;
   }
}
