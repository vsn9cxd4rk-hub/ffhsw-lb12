package go;


public class Ausbildung_Plan {

   private int id;
   private int jahr;
   private int veranstaltungID;
   private int mitgliederGruppe;
   private int ausbildungKategorie;
   private String details;
   private int ausbilder1;
   private int ausbilder2;


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

   public int getAusbildungKategorie() {
      return this.ausbildungKategorie;
   }

   public void setAusbildungKategorie(int ausbildungKategorie) {
      this.ausbildungKategorie = ausbildungKategorie;
   }

   public String getDetails() {
      return this.details;
   }

   public void setDetails(String details) {
      this.details = details;
   }

   public int getAusbilder1() {
      return this.ausbilder1;
   }

   public void setAusbilder1(int ausbilder1) {
      this.ausbilder1 = ausbilder1;
   }

   public int getAusbilder2() {
      return this.ausbilder2;
   }

   public void setAusbilder2(int ausbilder2) {
      this.ausbilder2 = ausbilder2;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
