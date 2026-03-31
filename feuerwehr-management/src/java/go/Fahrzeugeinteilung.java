package go;


public class Fahrzeugeinteilung {

   private int id;
   private int veranstaltungID;
   private int kategorie;
   private int jahr;
   private int mitgliederID;
   private int fahrzeugID;
   private int position;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getMitgliederID() {
      return this.mitgliederID;
   }

   public void setMitgliederID(int mitgliederID) {
      this.mitgliederID = mitgliederID;
   }

   public int getFahrzeugID() {
      return this.fahrzeugID;
   }

   public void setFahrzeugID(int fahrzeugID) {
      this.fahrzeugID = fahrzeugID;
   }

   public int getPosition() {
      return this.position;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getJahr() {
      return this.jahr;
   }

   public void setJahr(int jahr) {
      this.jahr = jahr;
   }

   public int getKategorie() {
      return this.kategorie;
   }

   public void setKategorie(int kategorie) {
      this.kategorie = kategorie;
   }
}
