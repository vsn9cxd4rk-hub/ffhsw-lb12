package service;

import ao.HauptprogrammAO;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleUser;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.Utils;

public class BerechtigunsManager {

   public static int[] ber = (new TabelleBerechtigunggruppe()).getAll(0, 1);
   public static int[] ber2 = (new TabelleBerechtigunggruppe()).getAll(0, 2);
   public static int[] ber3 = (new TabelleBerechtigunggruppe()).getAll(0, 3);


   public static void berechtigungVeranstaltung() {
      if(ber[44] == 1) {
         HauptprogrammAO.buttonAnwesenheitNachtragen.setEnabled(true);
         HauptprogrammAO.buttonAnwesenheitNachtragen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAnwesenheitNachtragen.setEnabled(false);
         HauptprogrammAO.buttonAnwesenheitNachtragen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[45] == 1) {
         HauptprogrammAO.buttonAbwesenheitsgrundNachtragen.setEnabled(true);
         HauptprogrammAO.buttonAbwesenheitsgrundNachtragen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAbwesenheitsgrundNachtragen.setEnabled(false);
         HauptprogrammAO.buttonAbwesenheitsgrundNachtragen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[47] == 1) {
         HauptprogrammAO.buttonAusbildungsInhalt.setEnabled(true);
         HauptprogrammAO.buttonAusbildungsInhalt.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAusbildungsInhalt.setEnabled(false);
         HauptprogrammAO.buttonAusbildungsInhalt.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[50] == 1) {
         HauptprogrammAO.buttonFahrzeugeinteilungNachtragen.setEnabled(true);
         HauptprogrammAO.buttonFahrzeugeinteilungNachtragen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonFahrzeugeinteilungNachtragen.setEnabled(false);
         HauptprogrammAO.buttonFahrzeugeinteilungNachtragen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[68] == 1) {
         HauptprogrammAO.buttonAtemschutzpassEintrag.setEnabled(true);
         HauptprogrammAO.buttonAtemschutzpassEintrag.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAtemschutzpassEintrag.setEnabled(false);
         HauptprogrammAO.buttonAtemschutzpassEintrag.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[74] == 1) {
         HauptprogrammAO.buttonVeranstaltungEditieren.setEnabled(true);
         HauptprogrammAO.buttonVeranstaltungEditieren.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonVeranstaltungEditieren.setEnabled(false);
         HauptprogrammAO.buttonVeranstaltungEditieren.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[75] == 1) {
         HauptprogrammAO.buttonFahrtenbuch.setEnabled(true);
         HauptprogrammAO.buttonFahrtenbuch.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonFahrtenbuch.setEnabled(false);
         HauptprogrammAO.buttonFahrtenbuch.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[76] == 1) {
         HauptprogrammAO.buttonSchichtplaner.setEnabled(true);
         HauptprogrammAO.buttonSchichtplaner.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonSchichtplaner.setEnabled(false);
         HauptprogrammAO.buttonSchichtplaner.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[81] == 1) {
         HauptprogrammAO.buttonEinsatz.setEnabled(true);
         HauptprogrammAO.buttonEinsatz.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonEinsatz.setEnabled(false);
         HauptprogrammAO.buttonEinsatz.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[82] == 1) {
         HauptprogrammAO.buttonDienstabend.setEnabled(true);
         HauptprogrammAO.buttonDienstabend.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonDienstabend.setEnabled(false);
         HauptprogrammAO.buttonDienstabend.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[83] == 1) {
         HauptprogrammAO.buttonBSW.setEnabled(true);
         HauptprogrammAO.buttonBSW.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonBSW.setEnabled(false);
         HauptprogrammAO.buttonBSW.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[84] == 1) {
         HauptprogrammAO.buttonSonstige.setEnabled(true);
         HauptprogrammAO.buttonSonstige.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonSonstige.setEnabled(false);
         HauptprogrammAO.buttonSonstige.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[87] == 1) {
         HauptprogrammAO.buttonUrlaubsplaner.setEnabled(true);
         HauptprogrammAO.buttonUrlaubsplaner.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonUrlaubsplaner.setEnabled(false);
         HauptprogrammAO.buttonUrlaubsplaner.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber2[58] == 1) {
         HauptprogrammAO.buttonMitgliederVerfuegbarkeit.setEnabled(true);
         HauptprogrammAO.buttonMitgliederVerfuegbarkeit.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonMitgliederVerfuegbarkeit.setEnabled(false);
         HauptprogrammAO.buttonMitgliederVerfuegbarkeit.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber2[60] == 1) {
         HauptprogrammAO.buttonSchulungListe.setEnabled(true);
         HauptprogrammAO.buttonSchulungListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonSchulungListe.setEnabled(false);
         HauptprogrammAO.buttonSchulungListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

   }

   public static void berechtigungMitglieder() {
      if(ber[1] == 1) {
         HauptprogrammAO.buttonMitgliederVerwaltng.setEnabled(true);
         HauptprogrammAO.buttonMitgliederVerwaltng.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonMitgliederVerwaltng.setEnabled(false);
         HauptprogrammAO.buttonMitgliederVerwaltng.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[59] == 1) {
         HauptprogrammAO.mitgliederakte.setEnabled(true);
         HauptprogrammAO.mitgliederakte.setToolTipText((String)null);
      } else {
         HauptprogrammAO.mitgliederakte.setEnabled(false);
         HauptprogrammAO.mitgliederakte.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[48] == 1) {
         HauptprogrammAO.buttonFahrzeugverwaltung.setEnabled(true);
         HauptprogrammAO.buttonFahrzeugverwaltung.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonFahrzeugverwaltung.setEnabled(false);
         HauptprogrammAO.buttonFahrzeugverwaltung.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[60] == 1) {
         HauptprogrammAO.buttonFahrzeugakte.setEnabled(true);
         HauptprogrammAO.buttonFahrzeugakte.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonFahrzeugakte.setEnabled(false);
         HauptprogrammAO.buttonFahrzeugakte.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[70] == 1) {
         HauptprogrammAO.buttonAbrechnung.setEnabled(true);
         HauptprogrammAO.buttonAbrechnung.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAbrechnung.setEnabled(false);
         HauptprogrammAO.buttonAbrechnung.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[77] == 1) {
         HauptprogrammAO.buttonLaufbahnEintragen.setEnabled(true);
         HauptprogrammAO.buttonLaufbahnEintragen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonLaufbahnEintragen.setEnabled(false);
         HauptprogrammAO.buttonLaufbahnEintragen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[49] == 1) {
         HauptprogrammAO.buttonGeraetePrüfung.setEnabled(true);
         HauptprogrammAO.buttonGeraetePrüfung.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonGeraetePrüfung.setEnabled(false);
         HauptprogrammAO.buttonGeraetePrüfung.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

   }

   public static void berechtigungListen() {
      if(ber[3] == 1) {
         HauptprogrammAO.mitgliederListe.setEnabled(true);
         HauptprogrammAO.mitgliederListe.setToolTipText((String)null);
         HauptprogrammAO.geburtstagListe.setEnabled(true);
         HauptprogrammAO.geburtstagListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.mitgliederListe.setEnabled(false);
         HauptprogrammAO.geburtstagListe.setEnabled(false);
         HauptprogrammAO.mitgliederListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
         HauptprogrammAO.geburtstagListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[4] == 1) {
         HauptprogrammAO.einsatzListe.setEnabled(true);
         HauptprogrammAO.einsatzListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.einsatzListe.setEnabled(false);
         HauptprogrammAO.einsatzListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[5] == 1) {
         HauptprogrammAO.bswListe.setEnabled(true);
         HauptprogrammAO.bswListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.bswListe.setEnabled(false);
         HauptprogrammAO.bswListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[6] == 1) {
         HauptprogrammAO.lehrgangListe.setEnabled(true);
         HauptprogrammAO.lehrgangListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.lehrgangListe.setEnabled(false);
         HauptprogrammAO.lehrgangListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[7] == 1) {
         HauptprogrammAO.anwesenheitListe.setEnabled(true);
         HauptprogrammAO.anwesenheitListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.anwesenheitListe.setEnabled(false);
         HauptprogrammAO.anwesenheitListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[8] == 1) {
         HauptprogrammAO.arbeitgeberListe.setEnabled(true);
         HauptprogrammAO.arbeitgeberListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.arbeitgeberListe.setEnabled(false);
         HauptprogrammAO.arbeitgeberListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[9] == 1) {
         HauptprogrammAO.angehoerigenListe.setEnabled(true);
         HauptprogrammAO.angehoerigenListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.angehoerigenListe.setEnabled(false);
         HauptprogrammAO.angehoerigenListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[10] == 1) {
         HauptprogrammAO.untersuchungListe.setEnabled(true);
         HauptprogrammAO.untersuchungListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.untersuchungListe.setEnabled(false);
         HauptprogrammAO.untersuchungListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[11] == 1) {
         HauptprogrammAO.beteiligungUebersichtListe.setEnabled(true);
         HauptprogrammAO.beteiligungUebersichtListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.beteiligungUebersichtListe.setEnabled(false);
         HauptprogrammAO.beteiligungUebersichtListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[12] == 1) {
         HauptprogrammAO.mitgliederBankverbindungListe.setEnabled(true);
         HauptprogrammAO.mitgliederBankverbindungListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.mitgliederBankverbindungListe.setEnabled(false);
         HauptprogrammAO.mitgliederBankverbindungListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[13] == 1) {
         HauptprogrammAO.veranstaltungListe.setEnabled(true);
         HauptprogrammAO.veranstaltungListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.veranstaltungListe.setEnabled(false);
         HauptprogrammAO.veranstaltungListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[53] == 1) {
         HauptprogrammAO.lehrgangsmeldung.setEnabled(true);
         HauptprogrammAO.lehrgangsmeldung.setToolTipText((String)null);
      } else {
         HauptprogrammAO.lehrgangsmeldung.setEnabled(false);
         HauptprogrammAO.lehrgangsmeldung.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[64] == 1) {
         HauptprogrammAO.ausbildungplanListe.setEnabled(true);
         HauptprogrammAO.ausbildungplanListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.ausbildungplanListe.setEnabled(false);
         HauptprogrammAO.ausbildungplanListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[69] == 1) {
         HauptprogrammAO.atemschutzpass.setEnabled(true);
         HauptprogrammAO.atemschutzpass.setToolTipText((String)null);
      } else {
         HauptprogrammAO.atemschutzpass.setEnabled(false);
         HauptprogrammAO.atemschutzpass.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[78] == 1) {
         HauptprogrammAO.buttonFahrtenbuchListe.setEnabled(true);
         HauptprogrammAO.buttonFahrtenbuchListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonFahrtenbuchListe.setEnabled(false);
         HauptprogrammAO.buttonFahrtenbuchListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[79] == 1) {
         HauptprogrammAO.buttonSchichtplanListe.setEnabled(true);
         HauptprogrammAO.buttonSchichtplanListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonSchichtplanListe.setEnabled(false);
         HauptprogrammAO.buttonSchichtplanListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[80] == 1) {
         HauptprogrammAO.buttonLaufbahnListe.setEnabled(true);
         HauptprogrammAO.buttonLaufbahnListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonLaufbahnListe.setEnabled(false);
         HauptprogrammAO.buttonLaufbahnListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[87] == 1) {
         HauptprogrammAO.buttonUrlaubsplanListe.setEnabled(true);
         HauptprogrammAO.buttonUrlaubsplanListe.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonUrlaubsplanListe.setEnabled(false);
         HauptprogrammAO.buttonUrlaubsplanListe.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

   }

   public static void berechtigungDokumente() {
      if(ber[14] == 1) {
         HauptprogrammAO.dokumentenexplorer.setEnabled(true);
         HauptprogrammAO.dokumentenexplorer.setToolTipText((String)null);
      } else {
         HauptprogrammAO.dokumentenexplorer.setEnabled(false);
         HauptprogrammAO.dokumentenexplorer.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[15] == 1) {
         HauptprogrammAO.einsatzBericht.setEnabled(true);
         HauptprogrammAO.einsatzBericht.setToolTipText((String)null);
      } else {
         HauptprogrammAO.einsatzBericht.setEnabled(false);
         HauptprogrammAO.einsatzBericht.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[16] == 1) {
         HauptprogrammAO.verdienstausfallbescheinigung.setEnabled(true);
         HauptprogrammAO.verdienstausfallbescheinigung.setToolTipText((String)null);
      } else {
         HauptprogrammAO.verdienstausfallbescheinigung.setEnabled(false);
         HauptprogrammAO.verdienstausfallbescheinigung.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[17] == 1) {
         HauptprogrammAO.jahresberichtErstellen.setEnabled(true);
         HauptprogrammAO.jahresberichtErstellen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.jahresberichtErstellen.setEnabled(false);
         HauptprogrammAO.jahresberichtErstellen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[18] == 1) {
         HauptprogrammAO.briefErstellen.setEnabled(true);
         HauptprogrammAO.briefErstellen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.briefErstellen.setEnabled(false);
         HauptprogrammAO.briefErstellen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[19] == 1) {
         HauptprogrammAO.mängelmeldung.setEnabled(true);
         HauptprogrammAO.mängelmeldung.setToolTipText((String)null);
      } else {
         HauptprogrammAO.mängelmeldung.setEnabled(false);
         HauptprogrammAO.mängelmeldung.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[55] == 1) {
         HauptprogrammAO.emailModul.setEnabled(true);
         HauptprogrammAO.emailModul.setToolTipText((String)null);
      } else {
         HauptprogrammAO.emailModul.setEnabled(false);
         HauptprogrammAO.emailModul.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[56] == 1) {
         HauptprogrammAO.buttonBestandsliste.setEnabled(true);
         HauptprogrammAO.buttonBestandsliste.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonBestandsliste.setEnabled(false);
         HauptprogrammAO.buttonBestandsliste.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[63] == 1) {
         HauptprogrammAO.buttonAusbildungsplan.setEnabled(true);
         HauptprogrammAO.buttonAusbildungsplan.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAusbildungsplan.setEnabled(false);
         HauptprogrammAO.buttonAusbildungsplan.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[66] == 1) {
         HauptprogrammAO.buttonMängelmeldungBearbeiten.setEnabled(true);
         HauptprogrammAO.buttonMängelmeldungBearbeiten.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonMängelmeldungBearbeiten.setEnabled(false);
         HauptprogrammAO.buttonMängelmeldungBearbeiten.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[88] == 1) {
         HauptprogrammAO.buttonProtokoll.setEnabled(true);
         HauptprogrammAO.buttonProtokoll.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonProtokoll.setEnabled(false);
         HauptprogrammAO.buttonProtokoll.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

   }

   public static void berechtigungStatistik() {
      if(ber[20] == 1) {
         HauptprogrammAO.anwesennheitGesamt.setEnabled(true);
      } else {
         HauptprogrammAO.anwesennheitGesamt.setEnabled(false);
      }

      if(ber[21] == 1) {
         HauptprogrammAO.anwesenheitEinsatz.setEnabled(true);
      } else {
         HauptprogrammAO.anwesenheitEinsatz.setEnabled(false);
      }

      if(ber[22] == 1) {
         HauptprogrammAO.anweseneheitDienstabend.setEnabled(true);
      } else {
         HauptprogrammAO.anweseneheitDienstabend.setEnabled(false);
      }

      if(ber[23] == 1) {
         HauptprogrammAO.anweseneheitBrandsicherheitswachen.setEnabled(true);
      } else {
         HauptprogrammAO.anweseneheitBrandsicherheitswachen.setEnabled(false);
      }

      if(ber[24] == 1) {
         HauptprogrammAO.abwesenheitDienstStatistik.setEnabled(true);
      } else {
         HauptprogrammAO.abwesenheitDienstStatistik.setEnabled(false);
      }

      if(ber[25] == 1) {
         HauptprogrammAO.einsatzArt.setEnabled(true);
      } else {
         HauptprogrammAO.einsatzArt.setEnabled(false);
      }

      if(ber[26] == 1) {
         HauptprogrammAO.ausrueckezeiten.setEnabled(true);
      } else {
         HauptprogrammAO.ausrueckezeiten.setEnabled(false);
      }

      if(ber[27] == 1) {
         HauptprogrammAO.einsatzdauer.setEnabled(true);
      } else {
         HauptprogrammAO.einsatzdauer.setEnabled(false);
      }

      if(ber[28] == 1) {
         HauptprogrammAO.einsatzMannstunden.setEnabled(true);
      } else {
         HauptprogrammAO.einsatzMannstunden.setEnabled(false);
      }

      if(ber[29] == 1) {
         HauptprogrammAO.einsatzProMonat.setEnabled(true);
      } else {
         HauptprogrammAO.einsatzProMonat.setEnabled(false);
      }

      if(ber[30] == 1) {
         HauptprogrammAO.einsatzProStunde.setEnabled(true);
      } else {
         HauptprogrammAO.einsatzProStunde.setEnabled(false);
      }

      if(ber[31] == 1) {
         HauptprogrammAO.einsatzProWochentag.setEnabled(true);
      } else {
         HauptprogrammAO.einsatzProWochentag.setEnabled(false);
      }

      if(ber[32] == 1) {
         HauptprogrammAO.bswMannstunden.setEnabled(true);
      } else {
         HauptprogrammAO.bswMannstunden.setEnabled(false);
      }

      if(ber[33] == 1) {
         HauptprogrammAO.fehlalarme.setEnabled(true);
      } else {
         HauptprogrammAO.fehlalarme.setEnabled(false);
      }

      if(ber[34] == 1) {
         HauptprogrammAO.beteiligungByVeranstaltung.setEnabled(true);
      } else {
         HauptprogrammAO.beteiligungByVeranstaltung.setEnabled(false);
      }

      if(ber[35] == 1) {
         HauptprogrammAO.ausbildungsstatistik.setEnabled(true);
      } else {
         HauptprogrammAO.ausbildungsstatistik.setEnabled(false);
      }

      if(ber[36] == 1) {
         HauptprogrammAO.fahrzeugStatistik.setEnabled(true);
      } else {
         HauptprogrammAO.fahrzeugStatistik.setEnabled(false);
      }

      if(ber[37] == 1) {
         HauptprogrammAO.alarmfahrtDauer.setEnabled(true);
      } else {
         HauptprogrammAO.alarmfahrtDauer.setEnabled(false);
      }

      if(ber[61] == 1) {
         HauptprogrammAO.beteiligunsdauerStatistik.setEnabled(true);
      } else {
         HauptprogrammAO.beteiligunsdauerStatistik.setEnabled(false);
      }

   }

   public static void berechtigungOptionen() {
      if(ber[42] == 1) {
         HauptprogrammAO.buttonAbwesenheitsgrund.setEnabled(true);
         HauptprogrammAO.buttonAbwesenheitsgrund.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonAbwesenheitsgrund.setEnabled(false);
         HauptprogrammAO.buttonAbwesenheitsgrund.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[43] == 1) {
         HauptprogrammAO.buttonEinstellungenOeffnen.setEnabled(true);
         HauptprogrammAO.buttonEinstellungenOeffnen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonEinstellungenOeffnen.setEnabled(false);
         HauptprogrammAO.buttonEinstellungenOeffnen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[51] == 1) {
         HauptprogrammAO.buttonBenutzerAnlegen.setEnabled(true);
         HauptprogrammAO.buttonBenutzerAnlegen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonBenutzerAnlegen.setEnabled(false);
         HauptprogrammAO.buttonBenutzerAnlegen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[62] == 1) {
         HauptprogrammAO.dbBackup.setEnabled(true);
         HauptprogrammAO.dbBackup.setToolTipText((String)null);
      } else {
         HauptprogrammAO.dbBackup.setEnabled(false);
         HauptprogrammAO.dbBackup.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber[67] == 1) {
         HauptprogrammAO.lehrgangAnlegen.setEnabled(true);
         HauptprogrammAO.lehrgangAnlegen.setToolTipText((String)null);
      } else {
         HauptprogrammAO.lehrgangAnlegen.setEnabled(false);
         HauptprogrammAO.lehrgangAnlegen.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      if(ber2[55] == 1) {
         HauptprogrammAO.buttonFacebookPostKonfiguration.setEnabled(true);
         HauptprogrammAO.buttonFacebookPostKonfiguration.setToolTipText((String)null);
      } else {
         HauptprogrammAO.buttonFacebookPostKonfiguration.setEnabled(false);
         HauptprogrammAO.buttonFacebookPostKonfiguration.setToolTipText((String)Konstante.KEINE_BERECHTIGUNG_VERFÜGBAR);
      }

      try {
         if((new TabelleUser()).getUserGruppe(runApplication.loginName).equals("admin")) {
            HauptprogrammAO.textAdminButtons.setVisible(true);
            HauptprogrammAO.buttonAdminGUI.setVisible(true);
         } else {
            HauptprogrammAO.textAdminButtons.setVisible(false);
            HauptprogrammAO.buttonAdminGUI.setVisible(false);
         }
      } catch (SQLException var1) {
         logging.logPrintStackTrace(var1);
      } catch (NullPointerException var2) {
         HauptprogrammAO.textAdminButtons.setVisible(false);
         HauptprogrammAO.buttonAdminGUI.setVisible(false);
      }

   }

   public static void setBevorzugteMitgliederGruppe() throws SQLException {
      int[] mGruppe = Utils.listToIntArray((new TabelleMitglieder_gruppe()).getAllGruppenIDs());
      runApplication.mitgliederGruppe = (new TabelleMitglieder_gruppe()).getGruppenName(mGruppe[0]);
      runApplication.mitgliederGruppeID = mGruppe[0];
   }
}
