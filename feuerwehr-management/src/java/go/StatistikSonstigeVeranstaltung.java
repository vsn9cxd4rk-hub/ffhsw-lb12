package go;


public class StatistikSonstigeVeranstaltung {

   private int id;
   private int veranstaltungID;
   private int kategorie;
   private int mitgliederGruppe;
   private int jahr;
   private int dauer;
   private int mannstunden;
   private int wochentag;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getKategorie() {
      return this.kategorie;
   }

   public void setKategorie(int kategorie) {
      this.kategorie = kategorie;
   }

   public int getJahr() {
      return this.jahr;
   }

   public void setJahr(int jahr) {
      this.jahr = jahr;
   }

   public int getDauer() {
      return this.dauer;
   }

   public void setDauer(int dauer) {
      this.dauer = dauer;
   }

   public int getMannstunden() {
      return this.mannstunden;
   }

   public void setMannstunden(int mannstunden) {
      this.mannstunden = mannstunden;
   }

   public int getWochentag() {
      return this.wochentag;
   }

   public void setWochentag(int wochentag) {
      this.wochentag = wochentag;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
