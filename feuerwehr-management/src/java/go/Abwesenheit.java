package go;


public class Abwesenheit {

   private int id;
   private int jahr;
   private int veranstaltungID;
   private int veranstaltungKategorie;
   private int mitgliederID;
   private int grund;


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

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getMitgliederID() {
      return this.mitgliederID;
   }

   public void setMitgliederID(int mitgliederID) {
      this.mitgliederID = mitgliederID;
   }

   public int getVeranstaltungKategorie() {
      return this.veranstaltungKategorie;
   }

   public void setVeranstaltungKategorie(int veranstaltungKategorie) {
      this.veranstaltungKategorie = veranstaltungKategorie;
   }

   public int getGrund() {
      return this.grund;
   }

   public void setGrund(int grund) {
      this.grund = grund;
   }
}
