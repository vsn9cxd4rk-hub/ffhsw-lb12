package go.schulung;


public class Schulung {

   private int id;
   private int jahr;
   private String name;
   private int gruppenID;
   private int minTeilnehmer;
   private int maxTeilnehmer;
   private String startDatum;
   private String endeDatum;


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

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getMinTeilnehmer() {
      return this.minTeilnehmer;
   }

   public void setMinTeilnehmer(int minTeilnehmer) {
      this.minTeilnehmer = minTeilnehmer;
   }

   public int getMaxTeilnehmer() {
      return this.maxTeilnehmer;
   }

   public void setMaxTeilnehmer(int maxTeilnehmer) {
      this.maxTeilnehmer = maxTeilnehmer;
   }

   public int getGruppenID() {
      return this.gruppenID;
   }

   public void setGruppenID(int gruppenID) {
      this.gruppenID = gruppenID;
   }

   public String getStartDatum() {
      return this.startDatum;
   }

   public void setStartDatum(String startDatum) {
      this.startDatum = startDatum;
   }

   public String getEndeDatum() {
      return this.endeDatum;
   }

   public void setEndeDatum(String endeDatum) {
      this.endeDatum = endeDatum;
   }
}
