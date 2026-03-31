package run.update;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class UpdateDatenbank {

   public void executeSql(String sql) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList executeSqlWithReturn(String sql) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int executeSqlWithReturnINT(String sql) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getInt(1):0;
   }

   public String executeSqlWithReturnString(String sql) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      return result.next()?result.getString(1):null;
   }
}
