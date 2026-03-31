package go;


public class Einsatz_bericht {

   private int id;
   private int einsatzNummer;
   private int veranstaltungID;
   private int jahr;
   private String dateiname;
   private int fahrzeugbelegung;
   private int atemschutz;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getEinsatzNummer() {
      return this.einsatzNummer;
   }

   public void setEinsatzNummer(int einsatzNummer) {
      this.einsatzNummer = einsatzNummer;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public String getDateiname() {
      return this.dateiname;
   }

   public void setDateiname(String dateiname) {
      this.dateiname = dateiname;
   }

   public int getJahr() {
      return this.jahr;
   }

   public void setJahr(int jahr) {
      this.jahr = jahr;
   }

   public int getFahrzeugbelegung() {
      return this.fahrzeugbelegung;
   }

   public void setFahrzeugbelegung(int fahrzeugbelegung) {
      this.fahrzeugbelegung = fahrzeugbelegung;
   }

   public int getAtemschutz() {
      return this.atemschutz;
   }

   public void setAtemschutz(int atemschutz) {
      this.atemschutz = atemschutz;
   }
}
