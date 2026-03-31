package go.karte;


public class AAO {

   private int id;
   private int stichwortID;
   private int strassenID;
   private int fahrzeugID;
   private int reihenfolge;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getStichwortID() {
      return this.stichwortID;
   }

   public void setStichwortID(int stichwortID) {
      this.stichwortID = stichwortID;
   }

   public int getStrassenID() {
      return this.strassenID;
   }

   public void setStrassenID(int strassenID) {
      this.strassenID = strassenID;
   }

   public int getFahrzeugID() {
      return this.fahrzeugID;
   }

   public void setFahrzeugID(int fahrzeugID) {
      this.fahrzeugID = fahrzeugID;
   }

   public int getReihenfolge() {
      return this.reihenfolge;
   }

   public void setReihenfolge(int reihenfolge) {
      this.reihenfolge = reihenfolge;
   }
}
