package go;


public class Jahresbericht {

   private int id;
   private int jahr;
   private String title;
   private String bericht;
   private String erstelldatum;
   private int autoBericht;
   private String dateiname;
   private String statistiken;
   private int mitgliederGruppe;


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

   public String getBericht() {
      return this.bericht;
   }

   public void setBericht(String bericht) {
      this.bericht = bericht;
   }

   public String getErstelldatum() {
      return this.erstelldatum;
   }

   public void setErstelldatum(String erstelldatum) {
      this.erstelldatum = erstelldatum;
   }

   public String getDateiname() {
      return this.dateiname;
   }

   public void setDateiname(String dateiname) {
      this.dateiname = dateiname;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public int getAutoBericht() {
      return this.autoBericht;
   }

   public void setAutoBericht(int autoBericht) {
      this.autoBericht = autoBericht;
   }

   public String getStatistiken() {
      return this.statistiken;
   }

   public void setStatistiken(String statistiken) {
      this.statistiken = statistiken;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
