/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.awt.DefaultFontMapper
 *  com.itextpdf.awt.FontMapper
 *  com.itextpdf.text.Document
 *  com.itextpdf.text.pdf.PdfContentByte
 *  com.itextpdf.text.pdf.PdfTemplate
 *  com.itextpdf.text.pdf.PdfWriter
 *  logging.logging
 *  org.jfree.chart.ChartUtilities
 *  org.jfree.chart.JFreeChart
 */
package utilities;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.awt.FontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import logging.logging;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class MyChartUtils {
    public static void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {
        PdfWriter writer = null;
        Document document = new Document();
        try {
            writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(fileName));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfTemplate template = contentByte.createTemplate((float)width, (float)height);
            Graphics2D graphics2d = template.createGraphics((float)width, (float)height, (FontMapper)new DefaultFontMapper());
            Rectangle2D.Double rectangle2d = new Rectangle2D.Double(0.0, 0.0, width, height);
            chart.draw(graphics2d, (Rectangle2D)rectangle2d);
            graphics2d.dispose();
            contentByte.addTemplate(template, 0.0f, 0.0f);
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
        document.close();
    }

    public static void writeChartToJPEG(JFreeChart chart, int width, int height, String fileName) {
        try {
            ChartUtilities.saveChartAsJPEG((File)new File(fileName), (JFreeChart)chart, (int)width, (int)height);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

