package go;

import com.itextpdf.text.Font.FontFamily;

public class DokumentLayoutOptions {

   private int NormalSchriftgröße;
   private FontFamily NormalSchriftart;


   public int getNormalSchriftgröße() {
      return this.NormalSchriftgröße;
   }

   public void setNormalSchriftgröße(int normalSchriftgröße) {
      this.NormalSchriftgröße = normalSchriftgröße;
   }

   public FontFamily getNormalSchriftart() {
      return this.NormalSchriftart;
   }

   public void setNormalSchriftart(FontFamily normalSchriftart) {
      this.NormalSchriftart = normalSchriftart;
   }
}
