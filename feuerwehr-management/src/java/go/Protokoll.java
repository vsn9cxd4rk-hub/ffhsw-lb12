package go;


public class Protokoll {

   private int id;
   private int veranstaltungID;
   private int jahr;
   private String title;
   private String protokolltext;
   private String erstelldatum;
   private int mitgliederGruppe;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getJahr() {
      return this.jahr;
   }

   public void setJahr(int jahr) {
      this.jahr = jahr;
   }

   public String getProtokolltext() {
      return this.protokolltext;
   }

   public void setProtokolltext(String protokolltext) {
      this.protokolltext = protokolltext;
   }

   public String getErstelldatum() {
      return this.erstelldatum;
   }

   public void setErstelldatum(String erstelldatum) {
      this.erstelldatum = erstelldatum;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
