package go;


public class Lehrgang {

   private int mitgliederID;
   private int lehrgangID;
   private int status;


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

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }
}
