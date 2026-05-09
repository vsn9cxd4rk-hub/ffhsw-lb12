/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.pdfview.PDFFile
 *  com.sun.pdfview.PDFPage
 *  com.sun.pdfview.PDFRenderer
 *  logging.logging
 */
package utilities;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.JOptionPane;
import logging.logging;

public class PDFPrinter {
    public static PrinterJob pjob;

    public PDFPrinter(String datei) {
        try {
            pjob = PrinterJob.getPrinterJob();
            if (pjob.printDialog()) {
                File file = new File(datei);
                FileInputStream fis = new FileInputStream(file);
                FileChannel fc = fis.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0L, fc.size());
                PDFFile pdfFile = new PDFFile((ByteBuffer)bb);
                PDFPrintPage pages = new PDFPrintPage(pdfFile);
                PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
                Paper a4paper = new Paper();
                double paperWidth = 8.26;
                double paperHeight = 11.69;
                a4paper.setSize(paperWidth * 72.0, paperHeight * 72.0);
                double leftMargin = 0.3;
                double rightMargin = 0.3;
                double topMargin = 0.5;
                double bottomMargin = 0.5;
                a4paper.setImageableArea(leftMargin * 72.0, topMargin * 72.0, (paperWidth - leftMargin - rightMargin) * 72.0, (paperHeight - topMargin - bottomMargin) * 72.0);
                pf.setPaper(a4paper);
                pjob.setJobName(file.getName());
                Book book = new Book();
                book.append(pages, pf, pdfFile.getNumPages());
                pjob.setPageable(book);
                pjob.print();
                logging.logInfo((Object)"Druckauftrag versendet...");
                fis.close();
                fc.close();
                ((ByteBuffer)bb).clear();
                file.delete();
                logging.logInfo((Object)"Schlie\u00dfe InputStrem und l\u00f6sche tempfile");
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Der Druck ist Fehlgeschlagen" + e.getMessage(), "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)e);
        }
        catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, "Der Druck ist Fehlgeschlagen" + e.getMessage(), "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)e);
        }
    }

    class PDFPrintPage
    implements Printable {
        private PDFFile file;

        PDFPrintPage(PDFFile file) {
            this.file = file;
        }

        @Override
        public int print(Graphics g, PageFormat format, int index) throws PrinterException {
            int pagenum = index + 1;
            if (pagenum >= 1 && pagenum <= this.file.getNumPages()) {
                Rectangle imgbounds;
                double paperaspect;
                Graphics2D g2 = (Graphics2D)g;
                PDFPage page = this.file.getPage(pagenum);
                double pwidth = format.getImageableWidth();
                double pheight = format.getImageableHeight();
                double aspect = page.getAspectRatio();
                if (aspect > (paperaspect = pwidth / pheight)) {
                    int height = (int)(pwidth / aspect);
                    imgbounds = new Rectangle((int)format.getImageableX(), (int)(format.getImageableY() + (pheight - (double)height) / 2.0), (int)pwidth, height);
                } else {
                    int width = (int)(pheight * aspect);
                    imgbounds = new Rectangle((int)(format.getImageableX() + (pwidth - (double)width) / 2.0), (int)format.getImageableY(), width, (int)pheight);
                }
                PDFRenderer pgs = new PDFRenderer(page, g2, imgbounds, null, null);
                try {
                    page.waitForFinish();
                    pgs.run();
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                return 0;
            }
            return 1;
        }
    }
}

