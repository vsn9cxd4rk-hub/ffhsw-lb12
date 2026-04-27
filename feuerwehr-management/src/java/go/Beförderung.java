package go;


public class Beförderung {

   private int id;
   private int dienstgradID;
   private int dienstgradVoraussetzung;
   private int zeit;
   private int dienstZeit;
   private int nurZeitBefoerderung;
   private int letzteStufe;
   private int auslassen;


   public int getDienstgradID() {
      return this.dienstgradID;
   }

   public void setDienstgradID(int dienstgradID) {
      this.dienstgradID = dienstgradID;
   }

   public int getDienstgradVoraussetzung() {
      return this.dienstgradVoraussetzung;
   }

   public void setDienstgradVoraussetzung(int dienstgradVoraussetzung) {
      this.dienstgradVoraussetzung = dienstgradVoraussetzung;
   }

   public int getZeit() {
      return this.zeit;
   }

   public void setZeit(int zeit) {
      this.zeit = zeit;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getNurZeitBefoerderung() {
      return this.nurZeitBefoerderung;
   }

   public void setNurZeitBefoerderung(int nurZeitBefoerderung) {
      this.nurZeitBefoerderung = nurZeitBefoerderung;
   }

   public int getLetzteStufe() {
      return this.letzteStufe;
   }

   public void setLetzteStufe(int letzteStufe) {
      this.letzteStufe = letzteStufe;
   }

   public int getAuslassen() {
      return this.auslassen;
   }

   public void setAuslassen(int auslassen) {
      this.auslassen = auslassen;
   }

   public int getDienstZeit() {
      return this.dienstZeit;
   }

   public void setDienstZeit(int dienstZeit) {
      this.dienstZeit = dienstZeit;
   }
}
