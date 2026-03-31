package go.urlaub;


public class Urlaub {

   private int id;
   private int jahr;
   private int mitgliederID;
   private int mitgliederGruppe;
   private String datumVon;
   private String datumBis;
   private int loeschkenner;


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

   public int getMitgliederID() {
      return this.mitgliederID;
   }

   public void setMitgliederID(int mitgliederID) {
      this.mitgliederID = mitgliederID;
   }

   public String getDatumVon() {
      return this.datumVon;
   }

   public void setDatumVon(String datumVon) {
      this.datumVon = datumVon;
   }

   public String getDatumBis() {
      return this.datumBis;
   }

   public void setDatumBis(String datumBis) {
      this.datumBis = datumBis;
   }

   public int getLoeschkenner() {
      return this.loeschkenner;
   }

   public void setLoeschkenner(int loeschkenner) {
      this.loeschkenner = loeschkenner;
   }

   public int getMitgliederGruppe() {
      return this.mitgliederGruppe;
   }

   public void setMitgliederGruppe(int mitgliederGruppe) {
      this.mitgliederGruppe = mitgliederGruppe;
   }
}
