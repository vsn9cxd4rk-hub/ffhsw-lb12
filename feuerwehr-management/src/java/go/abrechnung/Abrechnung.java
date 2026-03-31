package go.abrechnung;


public class Abrechnung {

   private int id;
   private int abrechnungID;
   private int artikelID;
   private int buchungskonto;
   private int zahlungsart;
   private int mitgliedID;
   private int jahr;
   private int veranstaltungID;
   private int veranstaltungKategorie;
   private int menge;
   private int wert;
   private String datum;
   private int status;
   private int umbuchungID;


   public int getAbrechnungID() {
      return this.abrechnungID;
   }

   public void setAbrechnungID(int abrechnungID) {
      this.abrechnungID = abrechnungID;
   }

   public int getMitgliedID() {
      return this.mitgliedID;
   }

   public void setMitgliedID(int mitgliedID) {
      this.mitgliedID = mitgliedID;
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

   public int getWert() {
      return this.wert;
   }

   public void setWert(int wert) {
      this.wert = wert;
   }

   public String getDatum() {
      return this.datum;
   }

   public void setDatum(String datum) {
      this.datum = datum;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public int getArtikelID() {
      return this.artikelID;
   }

   public void setArtikelID(int artikelID) {
      this.artikelID = artikelID;
   }

   public int getMenge() {
      return this.menge;
   }

   public void setMenge(int menge) {
      this.menge = menge;
   }

   public int getZahlungsart() {
      return this.zahlungsart;
   }

   public void setZahlungsart(int zahlungsart) {
      this.zahlungsart = zahlungsart;
   }

   public int getBuchungskonto() {
      return this.buchungskonto;
   }

   public void setBuchungskonto(int buchungskonto) {
      this.buchungskonto = buchungskonto;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getUmbuchungID() {
      return this.umbuchungID;
   }

   public void setUmbuchungID(int umbuchungID) {
      this.umbuchungID = umbuchungID;
   }
}
