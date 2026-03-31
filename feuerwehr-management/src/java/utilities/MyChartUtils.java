package utilities;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import logging.logging;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class MyChartUtils {

   public static void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {
      PdfWriter writer = null;
      Document document = new Document();

      try {
         writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
         document.open();
         PdfContentByte e = writer.getDirectContent();
         PdfTemplate template = e.createTemplate((float)width, (float)height);
         Graphics2D graphics2d = template.createGraphics((float)width, (float)height, new DefaultFontMapper());
         Double rectangle2d = new Double(0.0D, 0.0D, (double)width, (double)height);
         chart.draw(graphics2d, rectangle2d);
         graphics2d.dispose();
         e.addTemplate(template, 0.0F, 0.0F);
      } catch (Exception var10) {
         logging.logPrintStackTrace(var10);
      }

      document.close();
   }

   public static void writeChartToJPEG(JFreeChart chart, int width, int height, String fileName) {
      try {
         ChartUtilities.saveChartAsJPEG(new File(fileName), chart, width, height);
      } catch (IOException var5) {
         logging.logPrintStackTrace(var5);
      }

   }
}
