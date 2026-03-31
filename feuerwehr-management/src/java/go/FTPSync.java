package go;


public class FTPSync {

   private int id;
   private String clientID;
   private String datei;
   private String ordner;
   private int staus;
   private int stausDB;
   private long groeße;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getClientID() {
      return this.clientID;
   }

   public void setClientID(String clientID) {
      this.clientID = clientID;
   }

   public String getDatei() {
      return this.datei;
   }

   public void setDatei(String datei) {
      this.datei = datei;
   }

   public int getStatus() {
      return this.staus;
   }

   public void setStaus(int staus) {
      this.staus = staus;
   }

   public String getOrdner() {
      return this.ordner;
   }

   public void setOrdner(String ordner) {
      this.ordner = ordner;
   }

   public long getGroeße() {
      return this.groeße;
   }

   public void setGroeße(long groeße) {
      this.groeße = groeße;
   }

   public int getStausDB() {
      return this.stausDB;
   }

   public void setStausDB(int stausDB) {
      this.stausDB = stausDB;
   }
}
