package go.email;


public class Empfangen {

   private int id;
   private String sender;
   private String betreff;
   private String date;
   private int size;
   private Object nachricht;
   private int anhang;
   private int gelesen;
   private String art;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getSender() {
      return this.sender;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public String getBetreff() {
      return this.betreff;
   }

   public void setBetreff(String betreff) {
      this.betreff = betreff;
   }

   public String getDate() {
      return this.date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public int getSize() {
      return this.size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public Object getNachricht() {
      return this.nachricht;
   }

   public void setNachricht(Object nachricht) {
      this.nachricht = nachricht;
   }

   public int getAnhang() {
      return this.anhang;
   }

   public void setAnhang(int anhang) {
      this.anhang = anhang;
   }

   public int getGelesen() {
      return this.gelesen;
   }

   public void setGelesen(int gelesen) {
      this.gelesen = gelesen;
   }

   public String getArt() {
      return this.art;
   }

   public void setArt(String art) {
      this.art = art;
   }
}
