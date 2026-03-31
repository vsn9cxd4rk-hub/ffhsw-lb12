package utilities.facebook;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.restfb.types.GraphResponse;
import com.restfb.types.User;
import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleFacebook;
import data.tabellen.TabelleStichwort;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import go.Einsatz;
import go.StatistikEinsatz;
import go.Veranstaltung;
import java.awt.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import service.EMailService;
import utilities.Konstante;
import utilities.TimeCalculation;

public class Facebook {

   public void publishMessage(String nachricht, Veranstaltung veranstaltung, String postTyp) {
      try {
         String ex = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
         DefaultFacebookClient fbClient = new DefaultFacebookClient(ex);
         logging.logInfo("* Feed publishing *");
         FacebookType publishMessageResponse = (FacebookType)fbClient.publish("me/feed", FacebookType.class, new Parameter[]{Parameter.with("message", nachricht)});
         logging.logInfo("Published message ID: " + publishMessageResponse.getId());
         if(((String)runApplication.EINSTELLUNGEN.get("emailModul")).equals("1") && ((String)runApplication.EINSTELLUNGEN.get("facebookEMail")).equals("1")) {
            EMailService.EMailInformationServiceFacebook(nachricht, veranstaltung);
         }

         TabelleFacebook tabFacebook = new TabelleFacebook();
         go.Facebook facebook = new go.Facebook();
         facebook.setFbMessageID(publishMessageResponse.getId());
         facebook.setPostTyp(postTyp);
         facebook.setPostText(nachricht);
         facebook.setVeranstaltungID(veranstaltung.getId());
         facebook.setVeranstaltungKategorie(veranstaltung.getKategorie());
         facebook.setId(tabFacebook.getNextNummer());
         tabFacebook.insert(facebook);
      } catch (SQLException var9) {
         logging.logPrintStackTrace(var9);
      } catch (FacebookOAuthException var10) {
         logging.logError("Bei der Übertragung der Nachricht ist ein Fehler aufgetreten...");
         logging.logPrintStackTrace(var10);
      }

   }

   public String publishTestMessage(String nachricht) {
      String accessToken = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
      DefaultFacebookClient fbClient = new DefaultFacebookClient(accessToken);
      logging.logInfo("* Feed publishing *");
      FacebookType publishMessageResponse = (FacebookType)fbClient.publish("me/feed", FacebookType.class, new Parameter[]{Parameter.with("message", nachricht)});
      logging.logInfo("Published message ID: " + publishMessageResponse.getId());
      return publishMessageResponse.getId();
   }

   public void publishMessageWithPicture(String nachricht, String bild, String bildName, Veranstaltung veranstaltung, String postTyp) {
      try {
         runApplication.verarbeitungLäuft = 1;
         String ex = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
         DefaultFacebookClient fbClient = new DefaultFacebookClient(ex);
         logging.logInfo("* Feed publishing *");
         FileInputStream is = new FileInputStream(bild);
         FacebookType publishPhotoResponse = (FacebookType)fbClient.publish("me/photos", FacebookType.class, BinaryAttachment.with(bildName, is), new Parameter[]{Parameter.with("message", nachricht)});
         logging.logInfo("Published photo ID: " + publishPhotoResponse.getId());
         if(((String)runApplication.EINSTELLUNGEN.get("emailModul")).equals("1") && ((String)runApplication.EINSTELLUNGEN.get("facebookEMail")).equals("1")) {
            EMailService.EMailInformationServiceFacebook(nachricht, veranstaltung);
         }

         TabelleFacebook tabFacebook = new TabelleFacebook();
         go.Facebook facebook = new go.Facebook();
         facebook.setFbMessageID(publishPhotoResponse.getId());
         facebook.setPostTyp(postTyp);
         facebook.setPostText(nachricht);
         facebook.setVeranstaltungID(veranstaltung.getId());
         facebook.setVeranstaltungKategorie(veranstaltung.getKategorie());
         facebook.setId(tabFacebook.getNextNummer());
         tabFacebook.insert(facebook);
      } catch (IOException var17) {
         logging.logPrintStackTrace(var17);
      } catch (SQLException var18) {
         logging.logPrintStackTrace(var18);
      } catch (FacebookOAuthException var19) {
         logging.logError("Bei der Übertragung der Nachricht ist ein Fehler aufgetreten...");
         logging.logPrintStackTrace(var19);
      } finally {
         runApplication.verarbeitungLäuft = 0;
      }

   }

   public void publishScheduledMessage(String nachricht) {
      String accessToken = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
      DefaultFacebookClient fbClient = new DefaultFacebookClient(accessToken);
      logging.logInfo("* Feed publishing *");
      Date tomorrow = new Date(System.currentTimeMillis() + 86400000L);
      GraphResponse publishMessageResponse = (GraphResponse)fbClient.publish("me/feed", GraphResponse.class, new Parameter[]{Parameter.with("message", nachricht), Parameter.with("published", Boolean.valueOf(false)), Parameter.with("scheduled_publish_time", tomorrow)});
      logging.logInfo("Published (scheduled) message ID: " + publishMessageResponse.getId());
   }

   public void deletePublishedMessage(int veranstaltungID) {
      try {
         String ex = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
         DefaultFacebookClient fbClient = new DefaultFacebookClient(ex);
         TabelleFacebook tabFacebook = new TabelleFacebook();
         String messageID = tabFacebook.getFbMessageID(veranstaltungID);
         boolean deleted = fbClient.deleteObject(messageID, new Parameter[0]);
         logging.logInfo("Delete Published Facebook Post object --> " + deleted);
         tabFacebook.delete(messageID);
         logging.logInfo("FB-Message-ID wurde aus der DB gelöscht...");
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      } catch (FacebookOAuthException var8) {
         logging.logError("Bei der Übertragung der Nachricht ist ein Fehler aufgetreten...");
         logging.logPrintStackTrace(var8);
      }

   }

   public void deletePublishedMessageByID(String fbMessageID) {
      String accessToken = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
      DefaultFacebookClient fbClient = new DefaultFacebookClient(accessToken);
      boolean deleted = fbClient.deleteObject(fbMessageID, new Parameter[0]);
      logging.logInfo("Delete Published Facebook Post object --> " + deleted);
   }

   public String getUserInformations() {
      String accessToken = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
      DefaultFacebookClient fbClient = new DefaultFacebookClient(accessToken);
      User me = (User)fbClient.fetchObject("me", User.class, new Parameter[0]);
      return "Facebook-Benutzer:   " + me.getName() + "\nFacebook-ID:                " + me.getId() + "\n\n";
   }

   public void getExtendedAccesToken() {
      try {
         TabelleEinstellungen ex = new TabelleEinstellungen();
         TabelleEinstellungen_gespeichert einstellungen_gespeichert = new TabelleEinstellungen_gespeichert();
         String accessToken = (String)runApplication.EINSTELLUNGEN.get("facebookAccessToken");
         DefaultFacebookClient facebookClient = new DefaultFacebookClient(accessToken);
         AccessToken extendedAccessToken = facebookClient.obtainExtendedAccessToken((String)runApplication.EINSTELLUNGEN.get("facebookAppID"), (String)runApplication.EINSTELLUNGEN.get("facebookAppGeheimCode"), accessToken);
         ex.update("facebookAccessToken", extendedAccessToken.getAccessToken());
         einstellungen_gespeichert.update("facebookAccessTokenExpiereDate", extendedAccessToken.getExpires().toString());
         runApplication.EINSTELLUNGEN = ex.getAllEinstellungen();
         runApplication.EINSTELLUNGEN_GESPEICHERT = einstellungen_gespeichert.getAllEinstellungen();
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      } catch (FacebookOAuthException var7) {
         JOptionPane.showMessageDialog((Component)null, Konstante.FACEBOOK_FEHLER + "\n\n" + var7, "Fehlermeldung", 0);
         logging.logError("Bei der Übertragung der Nachricht ist ein Fehler aufgetreten...");
         logging.logPrintStackTrace(var7);
      }

   }

   public String createEinsatzPostString(Einsatz einsatz, StatistikEinsatz statistikEinsatz) {
      try {
         String e = (String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateEinsatz");
         e = e.replaceAll("<<EINSATZ_NUMMER>>", Integer.toString(einsatz.getEinsatznummer()));
         e = e.replaceAll("<<EINSATZ_DATUM>>", TimeCalculation.parseDateForGUI(einsatz.getDatum()));
         e = e.replaceAll("<<EINSATZ_JAHR>>", Integer.toString(statistikEinsatz.getJahr()));
         e = e.replaceAll("<<EINSATZ_ZEIT>>", einsatz.getZeitAlarm());
         e = e.replaceAll("<<EINSATZ_ORT>>", einsatz.getOrt());
         e = e.replaceAll("<<EINSATZ_STADTTEIL>>", einsatz.getStadtteil());
         e = e.replaceAll("<<EINSATZ_FAHRZEUG>>", einsatz.getFahrzeug());
         e = e.replaceAll("<<EINSATZ_KATEGORIE>>", (new TabelleEinsatz_kategorie()).getEinsatzKategorieName(statistikEinsatz.getKategorie()));
         e = e.replaceAll("<<EINSATZ_STICHWORT>>", (new TabelleStichwort()).getStichwortName(einsatz.getStichwort()));
         e = e.replaceAll("<<VERANSTALTUNG_ID>>", Integer.toString(einsatz.getVeranstaltungID()));
         return e;
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
         return null;
      }
   }

   public String createProtokollPostString(Einsatz einsatz, StatistikEinsatz statistikEinsatz, String protokoll) {
      try {
         String e = (String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateProtokoll");
         e = e.replaceAll("<<EINSATZ_NUMMER>>", Integer.toString(einsatz.getEinsatznummer()));
         e = e.replaceAll("<<EINSATZ_DATUM>>", TimeCalculation.parseDateForGUI(einsatz.getDatum()));
         e = e.replaceAll("<<EINSATZ_JAHR>>", Integer.toString(statistikEinsatz.getJahr()));
         e = e.replaceAll("<<EINSATZ_ZEIT>>", einsatz.getZeitAlarm());
         e = e.replaceAll("<<EINSATZ_ORT>>", einsatz.getOrt());
         e = e.replaceAll("<<EINSATZ_STADTTEIL>>", einsatz.getStadtteil());
         e = e.replaceAll("<<EINSATZ_FAHRZEUG>>", einsatz.getFahrzeug());
         e = e.replaceAll("<<EINSATZ_KATEGORIE>>", (new TabelleEinsatz_kategorie()).getEinsatzKategorieName(statistikEinsatz.getKategorie()));
         e = e.replaceAll("<<EINSATZ_STICHWORT>>", (new TabelleStichwort()).getStichwortName(einsatz.getStichwort()));
         e = e.replaceAll("<<VERANSTALTUNG_ID>>", Integer.toString(einsatz.getVeranstaltungID()));
         e = e.replaceAll("<<PROTOKOLL_TEXT>>", protokoll);
         return e;
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
         return null;
      }
   }
}
