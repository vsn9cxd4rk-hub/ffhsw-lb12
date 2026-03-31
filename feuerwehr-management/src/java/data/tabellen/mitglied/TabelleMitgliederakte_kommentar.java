package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.MitgliederFahrzeugAkte_Kommentar;
import java.sql.SQLException;
import java.sql.Statement;
import run.runApplication;

public class TabelleMitgliederakte_kommentar {

   public void insert(MitgliederFahrzeugAkte_Kommentar kommentar) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO mitgliederakte_kommentar (`id`, `datum`,`zeit`,`kommentar`, `mandantID`) VALUES (\'" + kommentar.getId() + "\', \'" + kommentar.getDatum() + "\', \'" + kommentar.getZeit() + "\', \'" + kommentar.getKommentar() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }
}
