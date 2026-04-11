package go.karte;


public class Straße {

   private int id;
   private String name;
   private String info;
   private String koordinaten;
   private String GPS_N;
   private String GPS_O;
   private String bild;
   private String bild2;
   private String PLZ;


   public String getPLZ() {
      return this.PLZ;
   }

   public void setPLZ(String pLZ) {
      this.PLZ = pLZ;
   }

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

   public String getInfo() {
      return this.info;
   }

   public void setInfo(String info) {
      this.info = info;
   }

   public String getKoordinaten() {
      return this.koordinaten;
   }

   public void setKoordinaten(String koordinaten) {
      this.koordinaten = koordinaten;
   }

   public String getBild() {
      return this.bild;
   }

   public void setBild(String bild) {
      this.bild = bild;
   }

   public String getBild2() {
      return this.bild2;
   }

   public void setBild2(String bild2) {
      this.bild2 = bild2;
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
