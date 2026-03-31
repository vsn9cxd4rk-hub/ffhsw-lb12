package go.schulung;


public class SchulungDetails {

   private int id;
   private int schulungID;
   private int jahr;
   private String datum;
   private int raumID;
   private String inhalt;
   private int fahrzeug1;
   private int fahrzeug2;


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

   public String getDatum() {
      return this.datum;
   }

   public void setDatum(String datum) {
      this.datum = datum;
   }

   public int getRaumID() {
      return this.raumID;
   }

   public void setRaumID(int raumID) {
      this.raumID = raumID;
   }

   public int getSchulungID() {
      return this.schulungID;
   }

   public void setSchulungID(int schulungID) {
      this.schulungID = schulungID;
   }

   public int getFahrzeug1() {
      return this.fahrzeug1;
   }

   public void setFahrzeug1(int fahrzeug1) {
      this.fahrzeug1 = fahrzeug1;
   }

   public int getFahrzeug2() {
      return this.fahrzeug2;
   }

   public void setFahrzeug2(int fahrzeug2) {
      this.fahrzeug2 = fahrzeug2;
   }

   public String getInhalt() {
      return this.inhalt;
   }

   public void setInhalt(String inhalt) {
      this.inhalt = inhalt;
   }
}
