package go;


public class Veranstaltung {

   private int id;
   private String name;
   private String name2;
   private String datum;
   private String zeit;
   private String zeitEnde;
   private int kategorie;
   private int mitgliederGruppe;
   private int fahrzeugeinteilung;
   private int infoVersandt;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDatum() {
      return this.datum;
   }

   public void setDatum(String datum) {
      this.datum = datum;
   }

   public String getZeit() {
      return this.zeit;
   }

   public void setZeit(String zeit) {
      this.zeit = zeit;
   }

   public int getKategorie() {
      return this.kategorie;
   }

   public void setKategorie(int kategorie) {
      this.kategorie = kategorie;
   }

   public int getFahrzeugeinteilung() {
      return this.fahrzeugeinteilung;
   }

   public void setFahrzeugeinteilung(int fahrzeugeinteilung) {
      this.fahrzeugeinteilung = fahrzeugeinteilung;
   }

   public String getZeitEnde() {
      return this.zeitEnde;
   }

   public void setZeitEnde(String zeitEnde) {
      this.zeitEnde = zeitEnde;
   }

   public int getInfoVersandt() {
      return this.infoVersandt;
   }

   public void setInfoVersandt(int infoVersandt) {
      this.infoVersandt = infoVersandt;
   }

   public String getName2() {
      return this.name2;
   }

   public void setName2(String name2) {
      this.name2 = name2;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
