package go;


public class SystemWarnung {

   private int id;
   private String datum;
   private String zeit;
   private String info;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
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

   public String getInfo() {
      return this.info;
   }

   public void setInfo(String info) {
      this.info = info;
   }
}
