package go;


public class Lehrgang_Kategorie {

   private int id;
   private String art;
   private String name;
   private int relevant;
   private int reihenfolge;
   private int ue;
   private int loeschbar;
   private int loeschkenner;


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

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getRelevant() {
      return this.relevant;
   }

   public void setRelevant(int relevant) {
      this.relevant = relevant;
   }

   public int getReihenfolge() {
      return this.reihenfolge;
   }

   public void setReihenfolge(int reihenfolge) {
      this.reihenfolge = reihenfolge;
   }

   public int getUe() {
      return this.ue;
   }

   public void setUe(int ue) {
      this.ue = ue;
   }

   public int getLoeschbar() {
      return this.loeschbar;
   }

   public void setLoeschbar(int loeschbar) {
      this.loeschbar = loeschbar;
   }

   public int getLoeschkenner() {
      return this.loeschkenner;
   }

   public void setLoeschkenner(int loeschkenner) {
      this.loeschkenner = loeschkenner;
   }
}
