package go;


public class Clients {

   private int id;
   private String clientID;
   private String alias;
   private String typ;
   private int online;
   private int zugelassen;


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

   public int getZugelassen() {
      return this.zugelassen;
   }

   public void setZugelassen(int zugelassen) {
      this.zugelassen = zugelassen;
   }

   public String getAlias() {
      return this.alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public String getTyp() {
      return this.typ;
   }

   public void setTyp(String typ) {
      this.typ = typ;
   }

   public int getOnline() {
      return this.online;
   }

   public void setOnline(int online) {
      this.online = online;
   }
}
