package go.bestandsliste;


public class Zuweisen {

   private int id;
   private int artikelId;
   private int anzahl;
   private String gruppe;
   private int mitgliedID;
   private String ort;
   private String produktion;
   private String identifikation;
   private String inDienst;
   private String pruefung;


   public int getArtikelId() {
      return this.artikelId;
   }

   public void setArtikelId(int artikelId) {
      this.artikelId = artikelId;
   }

   public int getAnzahl() {
      return this.anzahl;
   }

   public void setAnzahl(int anzahl) {
      this.anzahl = anzahl;
   }

   public int getMitgliedID() {
      return this.mitgliedID;
   }

   public void setMitgliedID(int wer) {
      this.mitgliedID = wer;
   }

   public String getGruppe() {
      return this.gruppe;
   }

   public void setGruppe(String gruppe) {
      this.gruppe = gruppe;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getOrt() {
      return this.ort;
   }

   public void setOrt(String ort) {
      this.ort = ort;
   }

   public String getProduktion() {
      return this.produktion;
   }

   public void setProduktion(String produktion) {
      this.produktion = produktion;
   }

   public String getIdentifikation() {
      return this.identifikation;
   }

   public void setIdentifikation(String identifikation) {
      this.identifikation = identifikation;
   }

   public String getInDienst() {
      return this.inDienst;
   }

   public void setInDienst(String inDienst) {
      this.inDienst = inDienst;
   }

   public String getPruefung() {
      return this.pruefung;
   }

   public void setPruefung(String pruefung) {
      this.pruefung = pruefung;
   }
}
