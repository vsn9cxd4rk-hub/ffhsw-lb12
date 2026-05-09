/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.icepdf.core.AnnotationCallback
 *  org.icepdf.ri.common.ComponentKeyBinding
 *  org.icepdf.ri.common.MyAnnotationCallback
 *  org.icepdf.ri.common.SwingController
 *  org.icepdf.ri.common.SwingViewBuilder
 */
package utilities;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logging.logging;
import org.icepdf.core.AnnotationCallback;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class PdfVierwer {
    public static void ViewerComponent(String datei) {
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JPanel viewerComponentPanel = factory.buildViewerPanel();
        ComponentKeyBinding.install((SwingController)controller, (JComponent)viewerComponentPanel);
        controller.getDocumentViewController().setAnnotationCallback((AnnotationCallback)new MyAnnotationCallback(controller.getDocumentViewController()));
        JFrame applicationFrame = new JFrame();
        applicationFrame.setDefaultCloseOperation(2);
        applicationFrame.getContentPane().add(viewerComponentPanel);
        controller.openDocument(datei);
        logging.logInfo((Object)("PDF wird ge\u00f6ffent" + datei));
        applicationFrame.pack();
        applicationFrame.setExtendedState(6);
        applicationFrame.setTitle("PdfViewer - " + datei);
        applicationFrame.setVisible(true);
    }
}

