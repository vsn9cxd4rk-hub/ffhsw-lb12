package go;


public class StatistikBSW {

   private int id;
   private int veranstaltungID;
   private int bswID;
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

   public int getBswID() {
      return this.bswID;
   }

   public void setBswID(int bswID) {
      this.bswID = bswID;
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

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getWochentag() {
      return this.wochentag;
   }

   public void setWochentag(int wochentag) {
      this.wochentag = wochentag;
   }
}
