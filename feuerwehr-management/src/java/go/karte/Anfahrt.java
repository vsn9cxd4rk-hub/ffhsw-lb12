package go.karte;


public class Anfahrt {

   private int id;
   private int strassenID;
   private int objektID;
   private String anfahrt;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getStrassenID() {
      return this.strassenID;
   }

   public void setStrassenID(int strassenID) {
      this.strassenID = strassenID;
   }

   public int getObjektID() {
      return this.objektID;
   }

   public void setObjektID(int objektID) {
      this.objektID = objektID;
   }

   public String getAnfahrt() {
      return this.anfahrt;
   }

   public void setAnfahrt(String anfahrt) {
      this.anfahrt = anfahrt;
   }
}
