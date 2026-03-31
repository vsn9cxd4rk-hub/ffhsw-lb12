package go;


public class Dienstgrad {

   private int id;
   private String beschreibung;
   private String beschreibungLang;


   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getBeschreibung() {
      return this.beschreibung;
   }

   public void setBeschreibung(String beschreibung) {
      this.beschreibung = beschreibung;
   }

   public String getBeschreibungLang() {
      return this.beschreibungLang;
   }

   public void setBeschreibungLang(String beschreibungLang) {
      this.beschreibungLang = beschreibungLang;
   }
}
