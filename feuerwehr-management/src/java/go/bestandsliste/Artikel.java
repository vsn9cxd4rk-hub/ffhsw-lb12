package go.bestandsliste;


public class Artikel {

   private int id;
   private String name;
   private String hersteller;
   private String typ;
   private String prüfinterval;
   private String beschreibung;
   private String bild;
   private int wert;
   private int EAN;
   private int loeschkenner;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getBeschreibung() {
      return this.beschreibung;
   }

   public void setBeschreibung(String beschreibung) {
      this.beschreibung = beschreibung;
   }

   public String getBild() {
      return this.bild;
   }

   public void setBild(String bild) {
      this.bild = bild;
   }

   public int getWert() {
      return this.wert;
   }

   public void setWert(int wert) {
      this.wert = wert;
   }

   public int getEAN() {
      return this.EAN;
   }

   public void setEAN(int eAN) {
      this.EAN = eAN;
   }

   public int getLoeschkenner() {
      return this.loeschkenner;
   }

   public void setLoeschkenner(int loeschkenner) {
      this.loeschkenner = loeschkenner;
   }

   public String getHersteller() {
      return this.hersteller;
   }

   public void setHersteller(String hersteller) {
      this.hersteller = hersteller;
   }

   public String getTyp() {
      return this.typ;
   }

   public void setTyp(String typ) {
      this.typ = typ;
   }

   public String getPrüfinterval() {
      return this.prüfinterval;
   }

   public void setPrüfinterval(String prüfinterval) {
      this.prüfinterval = prüfinterval;
   }
}
