package go;


public class Lehrgangsmeldung {

   private int id;
   private String lehrgang;
   private String art;


   public String getLehrgang() {
      return this.lehrgang;
   }

   public void setLehrgang(String lehrgang) {
      this.lehrgang = lehrgang;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getArt() {
      return this.art;
   }

   public void setArt(String art) {
      this.art = art;
   }
}
