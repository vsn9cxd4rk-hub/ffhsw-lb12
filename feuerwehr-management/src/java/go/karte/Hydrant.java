package go.karte;


public class Hydrant {

   private int id;
   private int strassenid;
   private String hausnummer;
   private int hausnummerID;
   private int nennweite;
   private String GPS_N;
   private String GPS_O;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getStrassenid() {
      return this.strassenid;
   }

   public void setStrassenid(int strassenid) {
      this.strassenid = strassenid;
   }

   public String getHausnummer() {
      return this.hausnummer;
   }

   public void setHausnummer(String hausnummer) {
      this.hausnummer = hausnummer;
   }

   public int getNennweite() {
      return this.nennweite;
   }

   public void setNennweite(int nennweite) {
      this.nennweite = nennweite;
   }

   public int getHausnummerID() {
      return this.hausnummerID;
   }

   public void setHausnummerID(int hausnummerID) {
      this.hausnummerID = hausnummerID;
   }

   public String getGPS_N() {
      return this.GPS_N;
   }

   public void setGPS_N(String gPS_N) {
      this.GPS_N = gPS_N;
   }

   public String getGPS_O() {
      return this.GPS_O;
   }

   public void setGPS_O(String gPS_O) {
      this.GPS_O = gPS_O;
   }
}
