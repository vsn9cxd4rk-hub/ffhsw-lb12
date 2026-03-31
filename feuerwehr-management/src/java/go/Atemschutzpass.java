package go;


public class Atemschutzpass {

   private int id;
   private int jahr;
   private int veranstaltungID;
   private int veranstaltungKategorie;
   private int mitgliederID;
   private int zeit;
   private int einsatzart;
   private int truppZuordnung;
   private String einsatzDetails;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getJahr() {
      return this.jahr;
   }

   public void setJahr(int jahr) {
      this.jahr = jahr;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getVeranstaltungKategorie() {
      return this.veranstaltungKategorie;
   }

   public void setVeranstaltungKategorie(int veranstaltungKategorie) {
      this.veranstaltungKategorie = veranstaltungKategorie;
   }

   public int getMitgliederID() {
      return this.mitgliederID;
   }

   public void setMitgliederID(int mitgliederID) {
      this.mitgliederID = mitgliederID;
   }

   public int getEinsatzart() {
      return this.einsatzart;
   }

   public void setEinsatzart(int einsatzart) {
      this.einsatzart = einsatzart;
   }

   public int getZeit() {
      return this.zeit;
   }

   public void setZeit(int zeit) {
      this.zeit = zeit;
   }

   public int getTruppZuordnung() {
      return this.truppZuordnung;
   }

   public void setTruppZuordnung(int truppZuordnung) {
      this.truppZuordnung = truppZuordnung;
   }

   public String getEinsatzDetails() {
      return this.einsatzDetails;
   }

   public void setEinsatzDetails(String einsatzDetails) {
      this.einsatzDetails = einsatzDetails;
   }
}
