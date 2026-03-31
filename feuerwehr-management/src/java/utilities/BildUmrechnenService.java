package utilities;

import com.itextpdf.text.Image;
import javax.swing.ImageIcon;

public class BildUmrechnenService {

   public static int bildBreiteVerkleinernPDF(Image img, int sollbreite) {
      double hoehe = (double)img.getHeight();
      double breite = (double)img.getWidth();
      int reduzierung;
      int reduziertebreite;
      if(hoehe >= breite) {
         reduzierung = (new Double(45000.0D / hoehe)).intValue();
         reduziertebreite = (new Double(hoehe / 100.0D * (double)reduzierung)).intValue();
         return reduziertebreite;
      } else {
         reduzierung = (new Double((double)(sollbreite * 100) / breite)).intValue();
         reduziertebreite = (new Double(breite / 100.0D * (double)reduzierung)).intValue();
         return reduziertebreite;
      }
   }

   public static int bildHoeheVerkleinernPDF(Image img, int sollbreite) {
      double hoehe = (double)img.getHeight();
      double breite = (double)img.getWidth();
      int reduzierung;
      int reduziertehoehe;
      if(hoehe >= breite) {
         reduzierung = (new Double(45000.0D / hoehe)).intValue();
         reduziertehoehe = (new Double(hoehe / 100.0D * (double)reduzierung)).intValue();
         return reduziertehoehe;
      } else {
         reduzierung = (new Double((double)(sollbreite * 100) / breite)).intValue();
         reduziertehoehe = (new Double(hoehe / 100.0D * (double)reduzierung)).intValue();
         return reduziertehoehe;
      }
   }

   public static int bildBreiteVerkleinern(ImageIcon img, int sollbreite) {
      double hoehe = (double)img.getIconHeight();
      double breite = (double)img.getIconWidth();
      int reduzierung;
      int reduziertebreite;
      if(hoehe >= breite) {
         reduzierung = (new Double(60000.0D / hoehe)).intValue();
         reduziertebreite = (new Double(hoehe / 100.0D * (double)reduzierung)).intValue();
         return reduziertebreite;
      } else {
         reduzierung = (new Double((double)(sollbreite * 100) / breite)).intValue();
         reduziertebreite = (new Double(breite / 100.0D * (double)reduzierung)).intValue();
         return reduziertebreite;
      }
   }

   public static int bildHoeheVerkleinern(ImageIcon img, int sollbreite) {
      double hoehe = (double)img.getIconHeight();
      double breite = (double)img.getIconWidth();
      int reduzierung;
      int reduziertehoehe;
      if(hoehe >= breite) {
         reduzierung = (new Double(60000.0D / hoehe)).intValue();
         reduziertehoehe = (new Double(hoehe / 100.0D * (double)reduzierung)).intValue();
         return reduziertehoehe;
      } else {
         reduzierung = (new Double((double)(sollbreite * 100) / breite)).intValue();
         reduziertehoehe = (new Double(hoehe / 100.0D * (double)reduzierung)).intValue();
         return reduziertehoehe;
      }
   }
}
