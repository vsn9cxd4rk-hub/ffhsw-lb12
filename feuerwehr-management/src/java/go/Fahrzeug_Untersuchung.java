package go;


public class Fahrzeug_Untersuchung {

   private int id;
   private String tüv;
   private String sp;
   private String service;
   private String gaswartung;
   private int infoTuev;
   private int infoSP;
   private int infoService;
   private int infoGas;
   private int mandantID;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getTüv() {
      return this.tüv;
   }

   public void setTüv(String tüv) {
      this.tüv = tüv;
   }

   public String getSp() {
      return this.sp;
   }

   public void setSp(String sp) {
      this.sp = sp;
   }

   public String getService() {
      return this.service;
   }

   public void setService(String service) {
      this.service = service;
   }

   public int getInfoTuev() {
      return this.infoTuev;
   }

   public void setInfoTuev(int infoTuev) {
      this.infoTuev = infoTuev;
   }

   public int getInfoSP() {
      return this.infoSP;
   }

   public void setInfoSP(int infoSP) {
      this.infoSP = infoSP;
   }

   public int getInfoService() {
      return this.infoService;
   }

   public void setInfoService(int infoService) {
      this.infoService = infoService;
   }

   public String getGaswartung() {
      return this.gaswartung;
   }

   public void setGaswartung(String gaswartung) {
      this.gaswartung = gaswartung;
   }

   public int getInfoGas() {
      return this.infoGas;
   }

   public void setInfoGas(int infoGas) {
      this.infoGas = infoGas;
   }

   public int getMandantID() {
      return this.mandantID;
   }

   public void setMandantID(int mandantID) {
      this.mandantID = mandantID;
   }
}
