package go.schulung;


public class SchulungTeilnehmer {

   private int id;
   private int mitgliedID;
   private int teilnehmerMandant;
   private int schulungID;
   private int status;
   private String statusGrund;
   private String statusDatum;
   private String statusZeit;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getMitgliedID() {
      return this.mitgliedID;
   }

   public void setMitgliedID(int mitgliedID) {
      this.mitgliedID = mitgliedID;
   }

   public int getSchulungID() {
      return this.schulungID;
   }

   public void setSchulungID(int schulungID) {
      this.schulungID = schulungID;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public String getStatusDatum() {
      return this.statusDatum;
   }

   public void setStatusDatum(String statusDatum) {
      this.statusDatum = statusDatum;
   }

   public String getStatusZeit() {
      return this.statusZeit;
   }

   public void setStatusZeit(String statusZeit) {
      this.statusZeit = statusZeit;
   }

   public int getTeilnehmerMandant() {
      return this.teilnehmerMandant;
   }

   public void setTeilnehmerMandant(int teilnehmerMandant) {
      this.teilnehmerMandant = teilnehmerMandant;
   }

   public String getStatusGrund() {
      return this.statusGrund;
   }

   public void setStatusGrund(String statusGrund) {
      this.statusGrund = statusGrund;
   }
}
