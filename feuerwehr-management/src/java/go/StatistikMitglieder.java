package go;


public class StatistikMitglieder {

   private int id;
   private int jahr;
   private int alter;
   private int anzahlGebTage;
   private int anzahl;
   private String erstellung;
   private int mitgliederGruppe;


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

   public int getAlter() {
      return this.alter;
   }

   public void setAlter(int alter) {
      this.alter = alter;
   }

   public int getAnzahl() {
      return this.anzahl;
   }

   public void setAnzahl(int anzahl) {
      this.anzahl = anzahl;
   }

   public String getErstellung() {
      return this.erstellung;
   }

   public void setErstellung(String erstellung) {
      this.erstellung = erstellung;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }

   public int getAnzahlGebTage() {
      return this.anzahlGebTage;
   }

   public void setAnzahlGebTage(int anzahlGebTage) {
      this.anzahlGebTage = anzahlGebTage;
   }
}
