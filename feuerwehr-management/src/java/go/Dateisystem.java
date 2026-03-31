package go;

import java.io.File;

public class Dateisystem {

   private int id;
   private File datei;


   public File getDatei() {
      return this.datei;
   }

   public void setDatei(File datei) {
      this.datei = datei;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
