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

      for(int statement = 0; statement < organisation.length; ++statement) {
         buildStatement.append("(\'");
         buildStatement.append(organisation[statement].getId());
         buildStatement.append("\', \'");
         buildStatement.append(organisation[statement].getVeranstaltungID());
         buildStatement.append("\', \'");
         buildStatement.append(organisation[statement].getOrganisationID());
         buildStatement.append("\', \'");
         buildStatement.append(organisation[statement].getStatus());
         buildStatement.append("\', \'");
         buildStatement.append((String)runApplication.PROPERTIES.get("MandantID"));
         if(statement != organisation.length - 1) {
            buildStatement.append("\'),");
         } else {
            buildStatement.append("\');");
         }
      }

      Statement var4 = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL(buildStatement.toString());
      var4.executeUpdate(buildStatement.toString());
   }

   public void insert(Einsatz_organisationen organisation) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO einsatz_organisationen (`id`, `veranstaltungID` , `organisationID`, `status`, `mandantID`) VALUES (\'" + organisation.getId() + "\', \'" + organisation.getVeranstaltungID() + "\', \'" + organisation.getOrganisationID() + "\', \'" + organisation.getStatus() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from einsatz_organisationen where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM einsatz_organisationen;");
      logging.logSQL("SELECT max(id) FROM einsatz_organisationen;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount(int organisationID, int status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_organisationen where organisationID = " + organisationID + " and status = " + status + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM einsatz_organisationen where organisationID = " + organisationID + " and status = " + status + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public boolean getStatusOfOrganisation(int veranstaltungID, int organisationID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT status FROM einsatz_organisationen where veranstaltungID = " + veranstaltungID + " and organisationID = " + organisationID + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT status FROM einsatz_organisationen where veranstaltungID = " + veranstaltungID + " and organisationID = " + organisationID + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) != 0:false;
   }

   public String getOrganisationIDKommaSeperated(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT eo.organisationID FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;");
      ResultSet result = statement.executeQuery("SELECT eo.organisationID FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      String[] idListe = Utils.listToArray(liste);
      StringBuilder build = new StringBuilder();
      if(idListe.length != 0) {
         for(int i = 0; i < idListe.length; ++i) {
            build.append(idListe[i]);
            if(i != idListe.length - 1) {
               build.append(",");
            }
         }
      } else {
         build.append("1");
      }

      logging.logSQL("getOrganisationIDKommaSeperated = " + build.toString());
      return build.toString();
   }

   public String getOrganisationNameKommaSeperated(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;");
      ResultSet result = statement.executeQuery("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.status = 1 and eo.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and o.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by o.sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      String[] idListe = Utils.listToArray(liste);
      StringBuilder build = new StringBuilder();
      if(idListe.length != 0) {
         for(int i = 0; i < idListe.length; ++i) {
            if(idListe[i].equals("") | idListe[i] == null) {
               build.append((String)runApplication.EINSTELLUNGEN.get("Name"));
            } else {
               build.append(idListe[i]);
            }

            if(i != idListe.length - 1) {
               build.append(", ");
            }
         }
      } else {
         build.append((String)runApplication.EINSTELLUNGEN.get("Name"));
      }

      logging.logSQL("getOrganisationIDKommaSeperated = " + build.toString());
      return build.toString();
   }

   public Vector getAnwesendeOrganisationen(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.organisationID > 1 and  eo.status = 1 and eo.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT o.name FROM einsatz_organisationen eo LEFT JOIN organisationen o ON eo.organisationID = o.id where eo.veranstaltungID = " + veranstaltungID + " and eo.organisationID > 1 and  eo.status = 1 and eo.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector anwesenheitListe = new Vector();
      anwesenheitListe.add(result.getString("name"));
      return anwesenheitListe;
   }
}
