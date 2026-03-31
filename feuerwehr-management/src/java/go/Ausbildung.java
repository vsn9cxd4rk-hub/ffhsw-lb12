package go;


public class Ausbildung {

   private int id;
   private int jahr;
   private int veranstaltungID;
   private int mitgliederGruppe;
   private int ausbildungKategorieID;
   private int mitgliederID;


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

   public int getAusbildungKategorieID() {
      return this.ausbildungKategorieID;
   }

   public void setAusbildungKategorieID(int ausbildungKategorieID) {
      this.ausbildungKategorieID = ausbildungKategorieID;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
