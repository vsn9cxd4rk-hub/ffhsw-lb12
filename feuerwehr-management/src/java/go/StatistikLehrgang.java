package go;


public class StatistikLehrgang {

   private int id;
   private int mitgliederID;
   private int jahr;
   private int lehrgangID;
   private int dauer;


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

   public int getLehrgangID() {
      return this.lehrgangID;
   }

   public void setLehrgangID(int lehrgangID) {
      this.lehrgangID = lehrgangID;
   }

   public int getDauer() {
      return this.dauer;
   }

   public void setDauer(int dauer) {
      this.dauer = dauer;
   }

   public int getJahr() {
      return this.jahr;
   }

   public void setJahr(int jahr) {
      this.jahr = jahr;
   }
}
