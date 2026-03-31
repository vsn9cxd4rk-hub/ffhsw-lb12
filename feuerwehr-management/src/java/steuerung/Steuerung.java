package steuerung;

import data.DatenbankZugriff;
import java.awt.Component;
import javax.swing.JOptionPane;
import logging.logging;
import steuerung.Status;
import steuerung.prozesse.AbwesenheitAnzeigen;
import steuerung.prozesse.AbwesenheitsgrundAnzeigen;
import steuerung.prozesse.AdministratorAnzeigen;
import steuerung.prozesse.AngehoerigeAnzeigen;
import steuerung.prozesse.AnmeldungAnzeigen;
import steuerung.prozesse.AnwesenheitEintragenAnzeigen;
import steuerung.prozesse.Anzeige;
import steuerung.prozesse.ArbeitgeberAnzeigen;
import steuerung.prozesse.AtemschutzpassEinsatzDetailsAnzeigen;
import steuerung.prozesse.AtemschutzpassEintragAnzeigen;
import steuerung.prozesse.AtemschutzpassListeAnzeigen;
import steuerung.prozesse.AusbildungAnzeigen;
import steuerung.prozesse.AusbildungKategorieAnlegenAnzeigen;
import steuerung.prozesse.AusbildungsinhalteTauschenAnzeigen;
import steuerung.prozesse.AusbildungsplanAnzeigen;
import steuerung.prozesse.BankverbindungAnzeigen;
import steuerung.prozesse.BeförderungAnzeigen;
import steuerung.prozesse.BenutzerAnlegenAnzeigen;
import steuerung.prozesse.BerechtigungAnzeigen;
import steuerung.prozesse.BrandsicherheitswacheEintragenAnzeigen;
import steuerung.prozesse.BriefAnzeigen;
import steuerung.prozesse.ClientsAnzeigen;
import steuerung.prozesse.DienstgradAnlegenAnzeigen;
import steuerung.prozesse.DokumentenexplorerAnzeigen;
import steuerung.prozesse.EMailNotificationAnzeigen;
import steuerung.prozesse.EinsatzBerichtAnzeigen;
import steuerung.prozesse.EinsatzEintragenAnzeigen;
import steuerung.prozesse.EinsatzListeAnzeigen;
import steuerung.prozesse.FacebookAPIKeyEinstellungenAnzeigen;
import steuerung.prozesse.FacebookPostKonfigurationAnzeigen;
import steuerung.prozesse.FahrtenbuchAnzeigen;
import steuerung.prozesse.FahrtenbuchListeAnzeigen;
import steuerung.prozesse.FahrzeugAnlegenAnzeigen;
import steuerung.prozesse.FahrzeugBelegungAnzeigen;
import steuerung.prozesse.FahrzeugEinteilungAnzeigen;
import steuerung.prozesse.FahrzeugGruppeAnzeigen;
import steuerung.prozesse.FahrzeugUntersuchungAnzeigen;
import steuerung.prozesse.FahrzeugakteAnzeigen;
import steuerung.prozesse.FahrzeugakteKomentarAnzeigen;
import steuerung.prozesse.FahrzeugeinteilungNachtragenAnzeigen;
import steuerung.prozesse.GeräteprüfungAnzeigen;
import steuerung.prozesse.HauptprogrammAnzeigen;
import steuerung.prozesse.JahresberichtAnzeigen;
import steuerung.prozesse.KategorienEditierenAnzeigen;
import steuerung.prozesse.LaufbahnEintragAnzeigen;
import steuerung.prozesse.LehrgangAnlegenAnzeigen;
import steuerung.prozesse.LehrgangZuordnungAnzeigen;
import steuerung.prozesse.LehrgangsmeldungAnzeigen;
import steuerung.prozesse.MeinPasswortAnzeigen;
import steuerung.prozesse.MitgliedAusserDienstAnzeigen;
import steuerung.prozesse.MitgliedLaufbahnListeAnzeigen;
import steuerung.prozesse.MitgliederAnlegenAnzeigen;
import steuerung.prozesse.MitgliederAnredeAnzeigen;
import steuerung.prozesse.MitgliederGruppeAnzeigen;
import steuerung.prozesse.MitgliederHistoryAnzeigen;
import steuerung.prozesse.MitgliederUntersuchungAnzeigen;
import steuerung.prozesse.MitgliederVerfuegbarkeiAnzeigen;
import steuerung.prozesse.MitgliederakteAnzeigen;
import steuerung.prozesse.MitgliederakteKomentarAnzeigen;
import steuerung.prozesse.MängelmeldungAnzeigen;
import steuerung.prozesse.MängelmeldungBearbeitenAnzeigen;
import steuerung.prozesse.MängelmeldungKommentarAnzeigen;
import steuerung.prozesse.ProtokollAnzeigen;
import steuerung.prozesse.ProzessbarAnzeigen;
import steuerung.prozesse.SchichtAnlegenAnzeigen;
import steuerung.prozesse.SchichtGruppeAnlegenAnzeigen;
import steuerung.prozesse.SchichtplanListeAnzeigen;
import steuerung.prozesse.SchichtplanerAnzeigen;
import steuerung.prozesse.StatistikAnzeigen;
import steuerung.prozesse.StichwortAnlegenAnzeigen;
import steuerung.prozesse.UrlaubsplanListeAnzeigen;
import steuerung.prozesse.UrlaubsplanerAnzeigen;
import steuerung.prozesse.VeranstaltungAnlegenAnzeigen;
import steuerung.prozesse.VeranstaltungEditierenAnzeigen;
import steuerung.prozesse.VeranstaltungKategorieAnlegenAnzeigen;
import steuerung.prozesse.VerdienstausfallAnzeigen;
import steuerung.prozesse.Verdienstausfall_ZeitenAnpassenAnzeigen;
import steuerung.prozesse.abrechnung.AbrechnungAnzeigen;
import steuerung.prozesse.abrechnung.AbrechnungArtikelAnzeigen;
import steuerung.prozesse.abrechnung.AbrechnungArtikelklasseAnzeigen;
import steuerung.prozesse.abrechnung.AbrechnungKontoAnzeigen;
import steuerung.prozesse.abrechnung.AbrechnungManuelleVerbuchungAnzeigen;
import steuerung.prozesse.abrechnung.AbrechnungMassenverbuchungAnzeigen;
import steuerung.prozesse.administrator.DebugAnzeigen;
import steuerung.prozesse.administrator.LogbuchAnzeigen;
import steuerung.prozesse.bestandsliste.ArtikelEintragenAnzeigen;
import steuerung.prozesse.bestandsliste.ArtikelZuweisenAnzeigen;
import steuerung.prozesse.bestandsliste.BestandVerschiebenAnzeigen;
import steuerung.prozesse.bestandsliste.BestandslisteAnzeigen;
import steuerung.prozesse.bestandsliste.LagerAnlegenAnzeigen;
import steuerung.prozesse.einstellungen.EinstellungAnzeigen;
import steuerung.prozesse.einstellungen.GrundkonfigurationAnzeigen;
import steuerung.prozesse.einstellungen.GrundkonfigurationJWSAnzeigen;
import steuerung.prozesse.einstellungen.LizenzAnzeigen;
import steuerung.prozesse.einstellungen.ProduktKeyEintragenAnzeigen;
import steuerung.prozesse.email.EMailModuleSignaturAnzeigen;
import steuerung.prozesse.email.EMail_AdressbauchAnzeigen;
import steuerung.prozesse.email.EmailAttachmentAnzeigen;
import steuerung.prozesse.email.EmailModulAnzeigen;
import steuerung.prozesse.email.NeueEmailAnzeigen;
import steuerung.prozesse.karte.AAOAnzeigen;
import steuerung.prozesse.karte.HydrantEintargenAnzeigen;
import steuerung.prozesse.karte.ImportAnzeigen;
import steuerung.prozesse.karte.KarteAnzeigen;
import steuerung.prozesse.karte.ObjektDokumenteAnzeigen;
import steuerung.prozesse.karte.ObjektEintargenAnzeigen;
import steuerung.prozesse.karte.ObjekthydrantenAuswählenAnzeigen;
import steuerung.prozesse.karte.ObjekthydrantenDetailsAnzeigen;
import steuerung.prozesse.karte.StraßenEintargenAnzeigen;
import steuerung.prozesse.listen.AngehoerigeListeAnzeigen;
import steuerung.prozesse.listen.AnwesenheitListeAnzeigen;
import steuerung.prozesse.listen.AnwesenheitListeOptionenAnzeigen;
import steuerung.prozesse.listen.AnwesenheitsTabelleProMitgliedAnzeigen;
import steuerung.prozesse.listen.ArbeitgeberListeAnzeigen;
import steuerung.prozesse.listen.AusbildungplanListeAnzeigen;
import steuerung.prozesse.listen.BeteiligungUebersichtListeAnzeigen;
import steuerung.prozesse.listen.BrandsicherheitswachenListeAnzeigen;
import steuerung.prozesse.listen.EhrungenKonfigurationAnzeigen;
import steuerung.prozesse.listen.GeburtstagListeAnzeigen;
import steuerung.prozesse.listen.LehrgangListeAnzeigen;
import steuerung.prozesse.listen.LehrgangListeOptionenAnzeigen;
import steuerung.prozesse.listen.MitgliederBankverbindungListeAnzeigen;
import steuerung.prozesse.listen.MitgliederListeAnzeigen;
import steuerung.prozesse.listen.MitgliederUntersuchungListeAnzeigen;
import steuerung.prozesse.listen.VeranstaltungListeAnzeigen;
import steuerung.prozesse.schulung.SchulungAnzeigen;
import steuerung.prozesse.schulung.SchulungBewerberAnzeigen;
import steuerung.prozesse.schulung.SchulungGruppeAnzeigen;
import steuerung.prozesse.schulung.SchulungListeAnzeigen;
import steuerung.prozesse.schulung.SchulungListeOptionenAnzeigen;
import steuerung.prozesse.schulung.SchulungRaumAnzeigen;
import steuerung.prozesse.utils.TerminDisplayAnzeigen;
import steuerung.unwetterwarnung.UnwetterwarnungAnzeigen;
import utilities.Konstante;
import utilities.logbuchEingabe;

public class Steuerung {

   private static Status status;


   public static void steuerung() {
      Object anzeige = null;
      if(!isStatus(Status.ENDE)) {
         if(isStatus(Status.HAUPTPROGRAMM)) {
            anzeige = new HauptprogrammAnzeigen();
         } else if(isStatus(Status.MITGLIEDER_ANLEGEN)) {
            anzeige = new MitgliederAnlegenAnzeigen();
         } else if(isStatus(Status.FAHREZUG_ANLEGEN)) {
            anzeige = new FahrzeugAnlegenAnzeigen();
         } else if(isStatus(Status.STICHWORT_ANLEGEN)) {
            anzeige = new StichwortAnlegenAnzeigen();
         } else if(isStatus(Status.EINSATZ_EINTRAGEN)) {
            anzeige = new EinsatzEintragenAnzeigen();
         } else if(isStatus(Status.VERANSTALTUNG_ANLEGEN)) {
            anzeige = new VeranstaltungAnlegenAnzeigen();
         } else if(isStatus(Status.VERANSTALTUNG_EDITIEREN)) {
            anzeige = new VeranstaltungEditierenAnzeigen();
         } else if(isStatus(Status.ANWESENHEIT_EINTRAGEN)) {
            anzeige = new AnwesenheitEintragenAnzeigen();
         } else if(isStatus(Status.VERANSTALTUNG_KATEGORIE_ANLEGEN)) {
            anzeige = new VeranstaltungKategorieAnlegenAnzeigen();
         } else if(isStatus(Status.FAHRZEUGEINTEILUNG)) {
            anzeige = new FahrzeugEinteilungAnzeigen();
         } else if(isStatus(Status.MITGLIEDER_LISTE)) {
            anzeige = new MitgliederListeAnzeigen();
         } else if(isStatus(Status.EINSATZ_LISTE)) {
            anzeige = new EinsatzListeAnzeigen();
         } else if(isStatus(Status.BSW_EINTARGEN)) {
            anzeige = new BrandsicherheitswacheEintragenAnzeigen();
         } else if(isStatus(Status.BSW_LISTE)) {
            anzeige = new BrandsicherheitswachenListeAnzeigen();
         } else if(isStatus(Status.LEHRGANG_LISTE)) {
            anzeige = new LehrgangListeAnzeigen();
         } else if(isStatus(Status.ANWESENHEIT_LISTE)) {
            anzeige = new AnwesenheitListeAnzeigen();
         } else if(isStatus(Status.GRUNDKONFIGURATION)) {
            anzeige = new GrundkonfigurationAnzeigen();
         } else if(isStatus(Status.GRUNDKONFIGURATION_JWS)) {
            anzeige = new GrundkonfigurationJWSAnzeigen();
         } else if(isStatus(Status.PROZESSBAR)) {
            anzeige = new ProzessbarAnzeigen();
         } else if(isStatus(Status.ARBEITGEBER_ANLEGEN)) {
            anzeige = new ArbeitgeberAnzeigen();
         } else if(isStatus(Status.ARBEITGEBER_LISTE)) {
            anzeige = new ArbeitgeberListeAnzeigen();
         } else if(isStatus(Status.ANGEHORIGE_ANLEGEN)) {
            anzeige = new AngehoerigeAnzeigen();
         } else if(isStatus(Status.ANGEHOERIGEN_LISTE)) {
            anzeige = new AngehoerigeListeAnzeigen();
         } else if(isStatus(Status.MITLIED_AUSSER_DIENST)) {
            anzeige = new MitgliedAusserDienstAnzeigen();
         } else if(isStatus(Status.STRAßE_EINTRAGEN)) {
            anzeige = new StraßenEintargenAnzeigen();
         } else if(isStatus(Status.HYDRANT_EINTARGEN)) {
            anzeige = new HydrantEintargenAnzeigen();
         } else if(isStatus(Status.IMPORT)) {
            anzeige = new ImportAnzeigen();
         } else if(isStatus(Status.ANMELDUNG)) {
            anzeige = new AnmeldungAnzeigen();
         } else if(isStatus(Status.KARTE)) {
            anzeige = new KarteAnzeigen();
         } else if(isStatus(Status.JAHRESBERICHT)) {
            anzeige = new JahresberichtAnzeigen();
         } else if(isStatus(Status.ABWESENHEITSGRUND_ANLEGEN)) {
            anzeige = new AbwesenheitsgrundAnzeigen();
         } else if(isStatus(Status.Abwesenheit)) {
            anzeige = new AbwesenheitAnzeigen();
         } else if(isStatus(Status.EINSTELLUNGEN)) {
            anzeige = new EinstellungAnzeigen();
         } else if(isStatus(Status.DOKUMENTENEXPLORER)) {
            anzeige = new DokumentenexplorerAnzeigen();
         } else if(isStatus(Status.MITGLIEDER_GRUPPE_ANLEGEN)) {
            anzeige = new MitgliederGruppeAnzeigen();
         } else if(isStatus(Status.BRIEF)) {
            anzeige = new BriefAnzeigen();
         } else if(isStatus(Status.EINSATZ_BERICHT)) {
            anzeige = new EinsatzBerichtAnzeigen();
         } else if(isStatus(Status.VERDIENSTAUSFALL)) {
            anzeige = new VerdienstausfallAnzeigen();
         } else if(isStatus(Status.UNTERSUCHUNG_ANLEGEN)) {
            anzeige = new MitgliederUntersuchungAnzeigen();
         } else if(isStatus(Status.UNTERSUCHUNG_LISTE)) {
            anzeige = new MitgliederUntersuchungListeAnzeigen();
         } else if(isStatus(Status.AUSBILDUNG_KATEGORIE)) {
            anzeige = new AusbildungKategorieAnlegenAnzeigen();
         } else if(isStatus(Status.AUSBILDUNGINHALT_EINTRAGEN)) {
            anzeige = new AusbildungAnzeigen();
         } else if(isStatus(Status.BETEILIGUNG_UEBERSICHT_LISTE)) {
            anzeige = new BeteiligungUebersichtListeAnzeigen();
         } else if(isStatus(Status.FAHRZUEG_UNTERSUCHUNG)) {
            anzeige = new FahrzeugUntersuchungAnzeigen();
         } else if(isStatus(Status.BANKVERBINDUNG_ANLEGEN)) {
            anzeige = new BankverbindungAnzeigen();
         } else if(isStatus(Status.GERAETEPRUEFUNG)) {
            anzeige = new GeräteprüfungAnzeigen();
         } else if(isStatus(Status.BANKVERBINDUNG_LISTE)) {
            anzeige = new MitgliederBankverbindungListeAnzeigen();
         } else if(isStatus(Status.MAENGELMELDUNG)) {
            anzeige = new MängelmeldungAnzeigen();
         } else if(isStatus(Status.VERANSTALTUNG_LISTE)) {
            anzeige = new VeranstaltungListeAnzeigen();
         } else if(isStatus(Status.FAHRZEUGEINTEILUNG_NACHTRAGEN)) {
            anzeige = new FahrzeugeinteilungNachtragenAnzeigen();
         } else if(isStatus(Status.BERECHTIGUNG)) {
            anzeige = new BerechtigungAnzeigen();
         } else if(isStatus(Status.BENUTZER_ANLEGEN)) {
            anzeige = new BenutzerAnlegenAnzeigen();
         } else if(isStatus(Status.LEHRGANGSMELDUNG)) {
            anzeige = new LehrgangsmeldungAnzeigen();
         } else if(isStatus(Status.NUTZUNGSHINWEISE)) {
            anzeige = new LizenzAnzeigen();
         } else if(isStatus(Status.VERDIENSTAUSFALL_ZEITENANPASSEN)) {
            anzeige = new Verdienstausfall_ZeitenAnpassenAnzeigen();
         } else if(isStatus(Status.ANREDE_ANLEGEN)) {
            anzeige = new MitgliederAnredeAnzeigen();
         } else if(isStatus(Status.MEIN_PASSWORT)) {
            anzeige = new MeinPasswortAnzeigen();
         } else if(isStatus(Status.EMAIL_MODUL)) {
            anzeige = new EmailModulAnzeigen();
         } else if(isStatus(Status.NEUE_EMAIL)) {
            anzeige = new NeueEmailAnzeigen();
         } else if(isStatus(Status.EMAIL_ADRESSBUCH)) {
            anzeige = new EMail_AdressbauchAnzeigen();
         } else if(isStatus(Status.BESTANDSLISTE)) {
            anzeige = new BestandslisteAnzeigen();
         } else if(isStatus(Status.ARTIKEL_EINTRAGEN)) {
            anzeige = new ArtikelEintragenAnzeigen();
         } else if(isStatus(Status.ARTIKEL_ZUWEISEN)) {
            anzeige = new ArtikelZuweisenAnzeigen();
         } else if(isStatus(Status.BESTAND_VERSCHIEBEN)) {
            anzeige = new BestandVerschiebenAnzeigen();
         } else if(isStatus(Status.NEUES_LAGER)) {
            anzeige = new LagerAnlegenAnzeigen();
         } else if(isStatus(Status.EMAIL_SIGNATUR)) {
            anzeige = new EMailModuleSignaturAnzeigen();
         } else if(isStatus(Status.EMAIL_ANHANG)) {
            anzeige = new EmailAttachmentAnzeigen();
         } else if(isStatus(Status.MITGLIEDERAKTE)) {
            anzeige = new MitgliederakteAnzeigen();
         } else if(isStatus(Status.FAHRZEUGAKTE)) {
            anzeige = new FahrzeugakteAnzeigen();
         } else if(isStatus(Status.AUSBILDUNGSPLAN)) {
            anzeige = new AusbildungsplanAnzeigen();
         } else if(isStatus(Status.AUSBILDUNGPLAN_LISTE)) {
            anzeige = new AusbildungplanListeAnzeigen();
         } else if(isStatus(Status.MITGLIEDERAKTE_KOMMENTAR)) {
            anzeige = new MitgliederakteKomentarAnzeigen();
         } else if(isStatus(Status.MAENGELMELDUNG_BEARBEITEN)) {
            anzeige = new MängelmeldungBearbeitenAnzeigen();
         } else if(isStatus(Status.LEHRGANG_ANLEGEN)) {
            anzeige = new LehrgangAnlegenAnzeigen();
         } else if(isStatus(Status.LEHRGANG_KONFIGURIEREN)) {
            anzeige = new LehrgangZuordnungAnzeigen();
         } else if(isStatus(Status.LIZENZ_KEY)) {
            anzeige = new ProduktKeyEintragenAnzeigen();
         } else if(isStatus(Status.FAHRZEUG_BESCHREIBUNG)) {
            anzeige = new FahrzeugGruppeAnzeigen();
         } else if(isStatus(Status.MAENGELMELDUNG_KOMMENTAR)) {
            anzeige = new MängelmeldungKommentarAnzeigen();
         } else if(isStatus(Status.DIENSTGRAD_ANLEGEN)) {
            anzeige = new DienstgradAnlegenAnzeigen();
         } else if(isStatus(Status.AUSBILDUNGSINHALTE_TAUSCHEN)) {
            anzeige = new AusbildungsinhalteTauschenAnzeigen();
         } else if(isStatus(Status.ATEMSCHUTZPASS_EINTRAG)) {
            anzeige = new AtemschutzpassEintragAnzeigen();
         } else if(isStatus(Status.ATEMSCHUTZPASS)) {
            anzeige = new AtemschutzpassListeAnzeigen();
         } else if(isStatus(Status.ABRECHNUNG)) {
            anzeige = new AbrechnungAnzeigen();
         } else if(isStatus(Status.ABRECHNUNG_ARTIKEL)) {
            anzeige = new AbrechnungArtikelAnzeigen();
         } else if(isStatus(Status.MANUELLE_VERBUCHUNG)) {
            anzeige = new AbrechnungManuelleVerbuchungAnzeigen();
         } else if(isStatus(Status.ARTIKELKLASSE_ANLEGEN)) {
            anzeige = new AbrechnungArtikelklasseAnzeigen();
         } else if(isStatus(Status.KONTO_ANLEGEN)) {
            anzeige = new AbrechnungKontoAnzeigen();
         } else if(isStatus(Status.GEBURTSTAG_LISTE)) {
            anzeige = new GeburtstagListeAnzeigen();
         } else if(isStatus(Status.DEBUG)) {
            anzeige = new DebugAnzeigen();
         } else if(isStatus(Status.LOGBUCH)) {
            anzeige = new LogbuchAnzeigen();
         } else if(isStatus(Status.SCHICHTPLANER)) {
            anzeige = new SchichtplanerAnzeigen();
         } else if(isStatus(Status.SCHICHT_ANLEGEN)) {
            anzeige = new SchichtAnlegenAnzeigen();
         } else if(isStatus(Status.SCHICHT_GRUPPE)) {
            anzeige = new SchichtGruppeAnlegenAnzeigen();
         } else if(isStatus(Status.SCHICHTPLAN_LISTE)) {
            anzeige = new SchichtplanListeAnzeigen();
         } else if(isStatus(Status.FAHRTENBUCH)) {
            anzeige = new FahrtenbuchAnzeigen();
         } else if(isStatus(Status.FAHRTENBUCH_LISTE)) {
            anzeige = new FahrtenbuchListeAnzeigen();
         } else if(isStatus(Status.MITGLIEDER_LAUFBAHN)) {
            anzeige = new MitgliedLaufbahnListeAnzeigen();
         } else if(isStatus(Status.LAUFBAHN_EINTRAG)) {
            anzeige = new LaufbahnEintragAnzeigen();
         } else if(isStatus(Status.ADMINBEREICH_STARTEN)) {
            anzeige = new AdministratorAnzeigen();
         } else if(isStatus(Status.URLAUBSPLANER)) {
            anzeige = new UrlaubsplanerAnzeigen();
         } else if(isStatus(Status.URLAUBSPLAN_LISTE)) {
            anzeige = new UrlaubsplanListeAnzeigen();
         } else if(isStatus(Status.STATISTIK)) {
            anzeige = new StatistikAnzeigen();
         } else if(isStatus(Status.BEFÖRDERUNG_KONFIGURIEREN)) {
            anzeige = new BeförderungAnzeigen();
         } else if(isStatus(Status.MITGLIEDER_HISTORY)) {
            anzeige = new MitgliederHistoryAnzeigen();
         } else if(isStatus(Status.CLIENTS)) {
            anzeige = new ClientsAnzeigen();
         } else if(isStatus(Status.KATEGORIEN_EDITIEREN)) {
            anzeige = new KategorienEditierenAnzeigen();
         } else if(isStatus(Status.LEHRGANG_LISTE_OPTIONEN)) {
            anzeige = new LehrgangListeOptionenAnzeigen();
         } else if(isStatus(Status.ANWESENHEIT_LISTE_OPTIONEN)) {
            anzeige = new AnwesenheitListeOptionenAnzeigen();
         } else if(isStatus(Status.PROTOKOLL)) {
            anzeige = new ProtokollAnzeigen();
         } else if(isStatus(Status.ANWESENHEITSTABELLE_PRO_MITGLIED)) {
            anzeige = new AnwesenheitsTabelleProMitgliedAnzeigen();
         } else if(isStatus(Status.EHRUNGEN_KONFIGURATION)) {
            anzeige = new EhrungenKonfigurationAnzeigen();
         } else if(isStatus(Status.UNWETTERWARNUNG)) {
            anzeige = new UnwetterwarnungAnzeigen();
         } else if(isStatus(Status.MASSENVERBUCHUNG)) {
            anzeige = new AbrechnungMassenverbuchungAnzeigen();
         } else if(isStatus(Status.MITGLIEDER_VERFUEGBARKEIT)) {
            anzeige = new MitgliederVerfuegbarkeiAnzeigen();
         } else if(isStatus(Status.FAHRZEUG_BELEGUNG)) {
            anzeige = new FahrzeugBelegungAnzeigen();
         } else if(isStatus(Status.VERANSTALTUNG_NOTIFICATON)) {
            anzeige = new EMailNotificationAnzeigen();
         } else if(isStatus(Status.TERMIN_DISPLAY)) {
            anzeige = new TerminDisplayAnzeigen();
         } else if(isStatus(Status.SCHULUNG)) {
            anzeige = new SchulungAnzeigen();
         } else if(isStatus(Status.SCHULUNG_RAUM)) {
            anzeige = new SchulungRaumAnzeigen();
         } else if(isStatus(Status.SCHULUNG_BEWERBER)) {
            anzeige = new SchulungBewerberAnzeigen();
         } else if(isStatus(Status.SCHULUNG_LISTE)) {
            anzeige = new SchulungListeAnzeigen();
         } else if(isStatus(Status.SCHULUNG_LISTE_OPTIONEN)) {
            anzeige = new SchulungListeOptionenAnzeigen();
         } else if(isStatus(Status.SCHULUNG_GRUPPE)) {
            anzeige = new SchulungGruppeAnzeigen();
         } else if(isStatus(Status.FAHRZEUGAKTE_KOMMENTAR)) {
            anzeige = new FahrzeugakteKomentarAnzeigen();
         } else if(isStatus(Status.ATEMSCHUTZPASS_EINSATZ_DETAILS)) {
            anzeige = new AtemschutzpassEinsatzDetailsAnzeigen();
         } else if(isStatus(Status.OBJEKT_EINTARGEN)) {
            anzeige = new ObjektEintargenAnzeigen();
         } else if(isStatus(Status.FACEBOOK_POST_KONFIGURATION)) {
            anzeige = new FacebookPostKonfigurationAnzeigen();
         } else if(isStatus(Status.FACEBOOK_API_KEY_EINSTELLUNGEN)) {
            anzeige = new FacebookAPIKeyEinstellungenAnzeigen();
         } else if(isStatus(Status.OBJEKT_HYDRANTEN_DETAILS)) {
            anzeige = new ObjekthydrantenDetailsAnzeigen();
         } else if(isStatus(Status.OBJEKT_HYDRANTEN_AUSWÄHLEN)) {
            anzeige = new ObjekthydrantenAuswählenAnzeigen();
         } else if(isStatus(Status.AAO)) {
            anzeige = new AAOAnzeigen();
         } else if(isStatus(Status.OBJEKT_DOKUMENTE)) {
            anzeige = new ObjektDokumenteAnzeigen();
         } else {
            JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_AUFRUFEN_MODUL, "Fehlermeldung", 0);
         }

         ((Anzeige)anzeige).ausfuehren();
      } else {
         logbuchEingabe.NeuerEintag("Programm wird beendet...");
         DatenbankZugriff.disconnectSSHServer();
         logging.logInfo("Programm wird beendet");
         System.exit(0);
      }

   }

   public static final void setStatus(Status status) {
      status = status;
   }

   public static final Status getStatus() {
      return status;
   }

   public static final boolean isStatus(Status status) {
      return status.equals(status);
   }
}
