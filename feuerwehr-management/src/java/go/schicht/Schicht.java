package go.schicht;


public class Schicht {

   private int id;
   private int jahr;
   private String name;
   private String schichtStartDatum;
   private String schichtStartUhrzeit;
   private String schichtEndeDatum;
   private String schichtEndeUhrzeit;
   private int minutenVon;
   private int minutenBis;


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

   public String getSchichtStartDatum() {
      return this.schichtStartDatum;
   }

   public void setSchichtStartDatum(String schichtStartDatum) {
      this.schichtStartDatum = schichtStartDatum;
   }

   public String getSchichtStartUhrzeit() {
      return this.schichtStartUhrzeit;
   }

   public void setSchichtStartUhrzeit(String schichtStartUhrzeit) {
      this.schichtStartUhrzeit = schichtStartUhrzeit;
   }

   public String getSchichtEndeDatum() {
      return this.schichtEndeDatum;
   }

   public void setSchichtEndeDatum(String schichtEndeDatum) {
      this.schichtEndeDatum = schichtEndeDatum;
   }

   public String getSchichtEndeUhrzeit() {
      return this.schichtEndeUhrzeit;
   }

   public void setSchichtEndeUhrzeit(String schichtEndeUhrzeit) {
      this.schichtEndeUhrzeit = schichtEndeUhrzeit;
   }

   public int getMinutenVon() {
      return this.minutenVon;
   }

   public void setMinutenVon(int minutenVon) {
      this.minutenVon = minutenVon;
   }

   public int getMinutenBis() {
      return this.minutenBis;
   }

   public void setMinutenBis(int minutenBis) {
      this.minutenBis = minutenBis;
   }
}
