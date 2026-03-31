package utilities;

import javax.swing.JFrame;
import javax.swing.JPanel;
import logging.logging;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.MyAnnotationCallback;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class PdfVierwer {

   public static void ViewerComponent(String datei) {
      SwingController controller = new SwingController();
      SwingViewBuilder factory = new SwingViewBuilder(controller);
      JPanel viewerComponentPanel = factory.buildViewerPanel();
      ComponentKeyBinding.install(controller, viewerComponentPanel);
      controller.getDocumentViewController().setAnnotationCallback(new MyAnnotationCallback(controller.getDocumentViewController()));
      JFrame applicationFrame = new JFrame();
      applicationFrame.setDefaultCloseOperation(2);
      applicationFrame.getContentPane().add(viewerComponentPanel);
      controller.openDocument(datei);
      logging.logInfo("PDF wird geöffent" + datei);
      applicationFrame.pack();
      applicationFrame.setExtendedState(6);
      applicationFrame.setTitle("PdfViewer - " + datei);
      applicationFrame.setVisible(true);
   }
}
