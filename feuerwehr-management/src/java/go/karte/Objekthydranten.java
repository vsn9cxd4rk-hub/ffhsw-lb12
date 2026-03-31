package go.karte;


public class Objekthydranten {

   private int id;
   private int objektID;
   private int hydrantID;
   private String entfernung;
   private String beschreibung;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getObjektID() {
      return this.objektID;
   }

   public void setObjektID(int objektID) {
      this.objektID = objektID;
   }

   public int getHydrantID() {
      return this.hydrantID;
   }

   public void setHydrantID(int hydrantID) {
      this.hydrantID = hydrantID;
   }

   public String getEntfernung() {
      return this.entfernung;
   }

   public void setEntfernung(String entfernung) {
      this.entfernung = entfernung;
   }

   public String getBeschreibung() {
      return this.beschreibung;
   }

   public void setBeschreibung(String beschreibung) {
      this.beschreibung = beschreibung;
   }
}
