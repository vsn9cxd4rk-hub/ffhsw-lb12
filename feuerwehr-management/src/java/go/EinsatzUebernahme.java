package go;


public class EinsatzUebernahme {

   private int id;
   private int veranstaltungID;
   private int FMSObjektID;
   private String straße;
   private String datum;
   private String zeit;
   private String stichwort;
   private String stadtteil;
   private String einsatznummerOffiziell;
   private String beschreibung;
   private String meldung;
   private int uebernommen;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getStraße() {
      return this.straße;
   }

   public void setStraße(String straße) {
      this.straße = straße;
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

   public String getStichwort() {
      return this.stichwort;
   }

   public void setStichwort(String stichwort) {
      this.stichwort = stichwort;
   }

   public String getStadtteil() {
      return this.stadtteil;
   }

   public void setStadtteil(String stadtteil) {
      this.stadtteil = stadtteil;
   }

   public String getEinsatznummerOffiziell() {
      return this.einsatznummerOffiziell;
   }

   public void setEinsatznummerOffiziell(String einsatznummerOffiziell) {
      this.einsatznummerOffiziell = einsatznummerOffiziell;
   }

   public String getBeschreibung() {
      return this.beschreibung;
   }

   public void setBeschreibung(String beschreibung) {
      this.beschreibung = beschreibung;
   }

   public String getMeldung() {
      return this.meldung;
   }

   public void setMeldung(String meldung) {
      this.meldung = meldung;
   }

   public int getUebernommen() {
      return this.uebernommen;
   }

   public void setUebernommen(int uebernommen) {
      this.uebernommen = uebernommen;
   }

   public int getVeranstaltungID() {
      return this.veranstaltungID;
   }

   public void setVeranstaltungID(int veranstaltungID) {
      this.veranstaltungID = veranstaltungID;
   }

   public int getFMSObjektID() {
      return this.FMSObjektID;
   }

   public void setFMSObjektID(int fMSObjektID) {
      this.FMSObjektID = fMSObjektID;
   }
}
