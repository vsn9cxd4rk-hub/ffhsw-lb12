package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import go.MitgliederFahrzeugAkte_Kommentar;
import java.sql.SQLException;
import java.sql.Statement;
import run.runApplication;

public class TabelleFahrzeugakte_kommentar {

   public void insert(MitgliederFahrzeugAkte_Kommentar kommentar) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO fahrzeugakte_kommentar (`id`, `datum`,`zeit`,`kommentar`, `mandantID`) VALUES (\'" + kommentar.getId() + "\', \'" + kommentar.getDatum() + "\', \'" + kommentar.getZeit() + "\', \'" + kommentar.getKommentar() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }
}
