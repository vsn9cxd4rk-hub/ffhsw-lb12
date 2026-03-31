package utilities;

import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleBriefe;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.abrechnung.TabelleAbrechnung;
import data.tabellen.abrechnung.TabelleAbrechnung_konto;
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.bestandsliste.TabelleLager_artikel;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeug_beschreibung;
import data.tabellen.karte.TabelleStrassen;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.schulung.TabelleSchulung;
import java.sql.SQLException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import utilities.Utils;

public class CreateTrees {

   public static DefaultTreeModel CreateEMailTree() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Inhalt");
      DefaultMutableTreeNode gesendet = new DefaultMutableTreeNode("Gesendete Objekte");
      DefaultMutableTreeNode postausgang = new DefaultMutableTreeNode("Postausgang");
      DefaultMutableTreeNode entwurf = new DefaultMutableTreeNode("Entwurf");
      root.add(postausgang);
      root.add(gesendet);
      root.add(entwurf);
      DefaultTreeModel treeModel = new DefaultTreeModel(root);
      return treeModel;
   }

   public static DefaultTreeModel CreateBestandslisteTree() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Inhalt");
      DefaultMutableTreeNode noteA = new DefaultMutableTreeNode("Artikel");
      DefaultMutableTreeNode noteL = new DefaultMutableTreeNode("Lager");
      DefaultMutableTreeNode noteF = new DefaultMutableTreeNode("Fahrzeuge");
      DefaultMutableTreeNode noteM = new DefaultMutableTreeNode("Mitglieder");
      root.add(noteA);
      root.add(noteL);
      root.add(noteF);
      root.add(noteM);
      TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleLager tabLager = new TabelleLager();

      try {
         String[] treeModel = Utils.listToArray(tabArtikel.getAllArtikel());
         String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
         String[] mitgliederListe = Utils.listToArray(tabMitglied.getAllMitgliederFromDataBase());
         String[] lagerListe = Utils.listToArray(tabLager.getAllLager());

         int m;
         DefaultMutableTreeNode note;
         for(m = 0; m < treeModel.length; ++m) {
            note = new DefaultMutableTreeNode(treeModel[m]);
            noteA.add(note);
         }

         for(m = 0; m < lagerListe.length; ++m) {
            note = new DefaultMutableTreeNode(lagerListe[m]);
            noteL.add(note);
         }

         for(m = 0; m < fahrzeugListe.length; ++m) {
            note = new DefaultMutableTreeNode(fahrzeugListe[m]);
            noteF.add(note);
         }

         for(m = 0; m < mitgliederListe.length; ++m) {
            note = new DefaultMutableTreeNode(mitgliederListe[m]);
            noteM.add(note);
         }
      } catch (SQLException var15) {
         logging.logPrintStackTrace(var15);
      }

      DefaultTreeModel var16 = new DefaultTreeModel(root);
      return var16;
   }

   public static DefaultTreeModel CreateTreeMitgliederListe(String suche) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mitglieder Liste");
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
      TabelleMitglied tabMitglieder = new TabelleMitglied();

      try {
         String[] treeModel = Utils.listToArray(tabGruppe.getAllGruppen());
         int[] mGruppeID = Utils.listToIntArray(tabGruppe.getAllGruppenIDs());

         for(int g = 0; g < treeModel.length; ++g) {
            DefaultMutableTreeNode noteGruppe = new DefaultMutableTreeNode(treeModel[g]);
            root.add(noteGruppe);
            String[] mitgliederListe = null;
            if(suche == null) {
               mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederEinerGruppeForTrees(mGruppeID[g]));
            } else {
               mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederEinerGruppeForSearch(mGruppeID[g], suche));
            }

            for(int m = 0; m < mitgliederListe.length; ++m) {
               DefaultMutableTreeNode note = new DefaultMutableTreeNode(mitgliederListe[m]);
               noteGruppe.add(note);
            }
         }
      } catch (SQLException var11) {
         logging.logPrintStackTrace(var11);
      }

      DefaultTreeModel var12 = new DefaultTreeModel(root);
      return var12;
   }

   public static DefaultTreeModel CreateTreeFahrzeugListe() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Fahrzeug Liste");
      TabelleFahrzeug_beschreibung tabBeschreibung = new TabelleFahrzeug_beschreibung();
      TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();

      try {
         String[] treeModel = Utils.listToArray(tabBeschreibung.getAllFahrzeugBeschreibungen());
         int[] fBeschreibungID = Utils.listToIntArray(tabBeschreibung.getAllFahrzeugBeschreibungenID());

         for(int b = 0; b < treeModel.length; ++b) {
            DefaultMutableTreeNode noteBeschreibung = new DefaultMutableTreeNode(treeModel[b]);
            root.add(noteBeschreibung);
            String[] fahrzeugListe = Utils.listToArray(tabFahrzeuge.getFahrzeugeByBeschreibungID(fBeschreibungID[b]));

            for(int f = 0; f < fahrzeugListe.length; ++f) {
               DefaultMutableTreeNode note = new DefaultMutableTreeNode(fahrzeugListe[f]);
               noteBeschreibung.add(note);
            }
         }
      } catch (SQLException var10) {
         logging.logPrintStackTrace(var10);
      }

      DefaultTreeModel var11 = new DefaultTreeModel(root);
      return var11;
   }

   public static DefaultTreeModel CreateTreeDokumentenListe() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Ordner Liste");
      TabelleJahr tabJahr = new TabelleJahr();
      DefaultMutableTreeNode treeModel;
      if(((String)runApplication.EINSTELLUNGEN.get("abrechnungModul")).equals("1") && BerechtigunsManager.ber2[39] == 1) {
         treeModel = new DefaultMutableTreeNode("Abrechnung");
         root.add(treeModel);
      }

      if(BerechtigunsManager.ber2[40] == 1) {
         treeModel = new DefaultMutableTreeNode("Ausbildungsunterlagen");
         root.add(treeModel);
      }

      if(BerechtigunsManager.ber2[41] == 1) {
         treeModel = new DefaultMutableTreeNode("Bestandsliste");
         root.add(treeModel);
      }

      if(BerechtigunsManager.ber2[42] == 1) {
         treeModel = new DefaultMutableTreeNode("Eigene Dateien");
         root.add(treeModel);
      }

      if(BerechtigunsManager.ber2[51] == 1) {
         treeModel = new DefaultMutableTreeNode("Atemschutz");
         root.add(treeModel);
      }

      try {
         String[] var7 = Utils.listToArray(tabJahr.getAllVerfügbarenJahre());

         for(int j = 0; j < var7.length; ++j) {
            DefaultMutableTreeNode noteJahre = new DefaultMutableTreeNode(var7[j]);
            root.add(noteJahre);
            DefaultMutableTreeNode noteOdner;
            if(BerechtigunsManager.ber2[50] == 1) {
               noteOdner = new DefaultMutableTreeNode("Berichte");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[49] == 1) {
               noteOdner = new DefaultMutableTreeNode("Beteiligungsübersicht");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[48] == 1) {
               noteOdner = new DefaultMutableTreeNode("Briefe");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[47] == 1) {
               noteOdner = new DefaultMutableTreeNode("Einsatzberichte");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[46] == 1) {
               noteOdner = new DefaultMutableTreeNode("Fahrzeugeinteilung");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[45] == 1) {
               noteOdner = new DefaultMutableTreeNode("Lehrgangsmeldungen");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[44] == 1) {
               noteOdner = new DefaultMutableTreeNode("Mängelmeldungen");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber2[43] == 1) {
               noteOdner = new DefaultMutableTreeNode("Verdienstausfallbescheinigung");
               noteJahre.add(noteOdner);
            }

            if(BerechtigunsManager.ber[89] == 1) {
               noteOdner = new DefaultMutableTreeNode("Protokoll");
               noteJahre.add(noteOdner);
            }

            if(((String)runApplication.EINSTELLUNGEN.get("Schichtplaner")).equals("1")) {
               noteOdner = new DefaultMutableTreeNode("Schichten");
               noteJahre.add(noteOdner);
            }
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      DefaultTreeModel var8 = new DefaultTreeModel(root);
      return var8;
   }

   public static DefaultTreeModel CreateTreeMaengelListe(String mandantID) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mängelmeldungen");
      TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
      TabelleJahr tabJahr = new TabelleJahr();

      try {
         String[] treeModel = Utils.listToArray(tabJahr.getAllVerfügbarenJahre());

         for(int j = 0; j < treeModel.length; ++j) {
            DefaultMutableTreeNode noteJahre = new DefaultMutableTreeNode(treeModel[j]);
            root.add(noteJahre);
            int aktellesJahr = Integer.parseInt(treeModel[j]);
            String[] maengelListeOffen = Utils.listToArray(tabMangel.getMaengelmeldungWithStatus(0, aktellesJahr, mandantID));
            String[] maengelListeGeschlossen = Utils.listToArray(tabMangel.getMaengelmeldungWithStatus(1, aktellesJahr, mandantID));
            DefaultMutableTreeNode noteOffene = new DefaultMutableTreeNode("Offene Mängelmeldungen");
            DefaultMutableTreeNode noteGeschlossene = new DefaultMutableTreeNode("Geschlossene Mängelmeldungen");
            noteJahre.add(noteOffene);
            noteJahre.add(noteGeschlossene);

            int g;
            DefaultMutableTreeNode noteGruppe2;
            for(g = 0; g < maengelListeOffen.length; ++g) {
               noteGruppe2 = new DefaultMutableTreeNode(maengelListeOffen[g]);
               noteOffene.add(noteGruppe2);
            }

            for(g = 0; g < maengelListeGeschlossen.length; ++g) {
               noteGruppe2 = new DefaultMutableTreeNode(maengelListeGeschlossen[g]);
               noteGeschlossene.add(noteGruppe2);
            }
         }
      } catch (SQLException var14) {
         logging.logPrintStackTrace(var14);
      }

      DefaultTreeModel var15 = new DefaultTreeModel(root);
      return var15;
   }

   public static DefaultTreeModel CreateTreeBriefeTemplates() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vorlagen");
      TabelleBriefe tabBrief = new TabelleBriefe();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

      try {
         int treeModel = tabGruppe.getID(runApplication.mitgliederGruppe);
         String[] templates = Utils.listToArray(tabBrief.getTemplates(treeModel));

         for(int t = 0; t < templates.length; ++t) {
            DefaultMutableTreeNode noteTemplate = new DefaultMutableTreeNode(templates[t]);
            root.add(noteTemplate);
         }
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      }

      DefaultTreeModel var8 = new DefaultTreeModel(root);
      return var8;
   }

   public static DefaultTreeModel CreateTreeJahresberichteTemplates(String mitgliederGruppe) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vorlagen");
      TabelleJahresbericht tabBericht = new TabelleJahresbericht();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

      try {
         String[] treeModel = Utils.listToArray(tabBericht.getAllTitle(tabGruppe.getID(mitgliederGruppe)));

         for(int t = 0; t < treeModel.length; ++t) {
            DefaultMutableTreeNode noteTemplate = new DefaultMutableTreeNode(treeModel[t]);
            root.add(noteTemplate);
         }
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      }

      DefaultTreeModel var8 = new DefaultTreeModel(root);
      return var8;
   }

   public static DefaultTreeModel CreateTreeAbrechnung() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Liste");
      TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
      TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
      TabelleMitglied tabMitglieder = new TabelleMitglied();

      try {
         DefaultMutableTreeNode treeModel = new DefaultMutableTreeNode("Kontobilanz");
         root.add(treeModel);
         String[] liste = Utils.listToArray(tabKonto.getAllKontos());

         DefaultMutableTreeNode noteAbrechnungen;
         for(int noteKonten = 0; noteKonten < liste.length; ++noteKonten) {
            noteAbrechnungen = new DefaultMutableTreeNode(liste[noteKonten]);
            treeModel.add(noteAbrechnungen);
         }

         DefaultMutableTreeNode var20 = new DefaultMutableTreeNode("Konten");
         root.add(var20);

         for(int var21 = 0; var21 < liste.length; ++var21) {
            DefaultMutableTreeNode listeAbrechnungen = new DefaultMutableTreeNode(liste[var21]);
            var20.add(listeAbrechnungen);
         }

         noteAbrechnungen = new DefaultMutableTreeNode("Abrechnungen");
         root.add(noteAbrechnungen);
         String[] var22 = Utils.listToArray(tabAbrechnung.getAllAbrechnungID());

         for(int noteMitglieder = 0; noteMitglieder < var22.length; ++noteMitglieder) {
            DefaultMutableTreeNode mGruppe = new DefaultMutableTreeNode(var22[noteMitglieder]);
            noteAbrechnungen.add(mGruppe);
         }

         DefaultMutableTreeNode var23 = new DefaultMutableTreeNode("Mitglieder");
         root.add(var23);
         String[] var24 = Utils.listToArray(tabGruppe.getAllGruppen());
         int[] mGruppeID = Utils.listToIntArray(tabGruppe.getAllGruppenIDs());

         for(int g = 0; g < var24.length; ++g) {
            DefaultMutableTreeNode noteGruppe = new DefaultMutableTreeNode(var24[g]);
            var23.add(noteGruppe);
            String[] mitgliederListe = null;
            mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederEinerGruppeForTrees(mGruppeID[g]));

            for(int m = 0; m < mitgliederListe.length; ++m) {
               DefaultMutableTreeNode note = new DefaultMutableTreeNode(mitgliederListe[m]);
               noteGruppe.add(note);
            }
         }
      } catch (SQLException var18) {
         logging.logPrintStackTrace(var18);
      }

      DefaultTreeModel var19 = new DefaultTreeModel(root);
      return var19;
   }

   public static DefaultTreeModel CreateTreeStatistik() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Statistik");
      DefaultMutableTreeNode noteAnwesenheit = new DefaultMutableTreeNode("Anwesenheit");
      root.add(noteAnwesenheit);
      DefaultMutableTreeNode noteAbwesenheit;
      DefaultMutableTreeNode noteAusbildung;
      if(BerechtigunsManager.ber[21] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Einsatz");
         noteAnwesenheit.add(noteAbwesenheit);
         noteAusbildung = new DefaultMutableTreeNode("Durchschnittliche Einsatzbeteiligung");
         noteAnwesenheit.add(noteAusbildung);
      }

      if(BerechtigunsManager.ber[22] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Dienst");
         noteAnwesenheit.add(noteAbwesenheit);
         noteAusbildung = new DefaultMutableTreeNode("Durchschnittliche Dienstbeteiligung");
         noteAnwesenheit.add(noteAusbildung);
      }

      if(BerechtigunsManager.ber[23] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("BSW");
         noteAnwesenheit.add(noteAbwesenheit);
      }

      if(BerechtigunsManager.ber2[26] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Sonstige Veranstaltungen");
         noteAnwesenheit.add(noteAbwesenheit);
      }

      if(BerechtigunsManager.ber[20] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Anwesenheit Gesamt");
         noteAnwesenheit.add(noteAbwesenheit);
         noteAusbildung = new DefaultMutableTreeNode("Anwesenheit Gesamt in %");
         noteAnwesenheit.add(noteAusbildung);
      }

      if(BerechtigunsManager.ber[61] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Beteiligungsdauer");
         noteAnwesenheit.add(noteAbwesenheit);
      }

      if(BerechtigunsManager.ber[34] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Beteiligung bei...");
         noteAnwesenheit.add(noteAbwesenheit);
      }

      if(BerechtigunsManager.ber2[27] == 1) {
         noteAbwesenheit = new DefaultMutableTreeNode("Verfügbarkeit Einsatz");
         noteAnwesenheit.add(noteAbwesenheit);
      }

      noteAbwesenheit = new DefaultMutableTreeNode("Abwesenheit");
      root.add(noteAbwesenheit);
      DefaultMutableTreeNode noteEinsatz;
      if(BerechtigunsManager.ber[24] == 1) {
         noteAusbildung = new DefaultMutableTreeNode("Abwesenheitsstatistik");
         noteAbwesenheit.add(noteAusbildung);
         noteEinsatz = new DefaultMutableTreeNode("An- / Abwesenheitsstatistik");
         noteAbwesenheit.add(noteEinsatz);
      }

      noteAusbildung = new DefaultMutableTreeNode("Ausbildung");
      root.add(noteAusbildung);
      DefaultMutableTreeNode noteMannstaunden;
      if(BerechtigunsManager.ber[35] == 1) {
         noteEinsatz = new DefaultMutableTreeNode("Ausbildungsstatistik");
         noteAusbildung.add(noteEinsatz);
         noteMannstaunden = new DefaultMutableTreeNode("Ausbilder Statistik");
         noteAusbildung.add(noteMannstaunden);
      }

      noteEinsatz = new DefaultMutableTreeNode("Einsatzstatistik");
      root.add(noteEinsatz);
      DefaultMutableTreeNode noteAtemschutz;
      if(BerechtigunsManager.ber[25] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Einsatzart");
         noteEinsatz.add(noteMannstaunden);
         noteAtemschutz = new DefaultMutableTreeNode("Stichwort Statistik");
         noteEinsatz.add(noteAtemschutz);
      }

      if(BerechtigunsManager.ber[26] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Ausrückezeiten");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[37] == 1 && ((String)runApplication.EINSTELLUNGEN.get("feldEintreffenAusblenden")).equals("0")) {
         noteMannstaunden = new DefaultMutableTreeNode("Alarmfahrtdauer");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[36] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Fahrzeuge im Einsatz");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[33] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Fehlalarme");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[27] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Durchschnittliche Einsatzdauer");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[19] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Einsatz pro Monat");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[31] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Einsatz Pro Wochentag");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber[30] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Einsatz pro Stunde");
         noteEinsatz.add(noteMannstaunden);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("feldStadtteilAusblenden")).equals("0") && BerechtigunsManager.ber2[28] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Stadtteil Statistik");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber2[29] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Schutzziel Statistik");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber2[30] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Tag / Nacht Einsätze");
         noteEinsatz.add(noteMannstaunden);
      }

      if(BerechtigunsManager.ber2[31] == 1) {
         noteMannstaunden = new DefaultMutableTreeNode("Fahrzeugbelegung");
         noteEinsatz.add(noteMannstaunden);
      }

      noteMannstaunden = new DefaultMutableTreeNode("Mannstunden");
      root.add(noteMannstaunden);
      DefaultMutableTreeNode noteVeranstaltung;
      if(BerechtigunsManager.ber[28] == 1) {
         noteAtemschutz = new DefaultMutableTreeNode("Einsatz Mannstunden");
         noteMannstaunden.add(noteAtemschutz);
         noteVeranstaltung = new DefaultMutableTreeNode("Einsatz Mannstunden pro Monat");
         noteMannstaunden.add(noteVeranstaltung);
      }

      if(BerechtigunsManager.ber[32] == 1) {
         noteAtemschutz = new DefaultMutableTreeNode("BSW Mannstunden");
         noteMannstaunden.add(noteAtemschutz);
         noteVeranstaltung = new DefaultMutableTreeNode("BSW Mannstunden pro Monat");
         noteMannstaunden.add(noteVeranstaltung);
      }

      if(BerechtigunsManager.ber[53] == 1) {
         noteAtemschutz = new DefaultMutableTreeNode("Sonstige Mannstunden");
         noteMannstaunden.add(noteAtemschutz);
      }

      noteAtemschutz = new DefaultMutableTreeNode("Atemschutz");
      root.add(noteAtemschutz);
      if(BerechtigunsManager.ber2[32] == 1) {
         noteVeranstaltung = new DefaultMutableTreeNode("Atemschutzstatistik");
         noteAtemschutz.add(noteVeranstaltung);
      }

      noteVeranstaltung = new DefaultMutableTreeNode("Veranstaltung");
      root.add(noteVeranstaltung);
      DefaultMutableTreeNode noteMitglieder;
      if(BerechtigunsManager.ber2[33] == 1) {
         noteMitglieder = new DefaultMutableTreeNode("Veranstaltungszählung");
         noteVeranstaltung.add(noteMitglieder);
      }

      noteMitglieder = new DefaultMutableTreeNode("Mitglieder");
      root.add(noteMitglieder);
      DefaultMutableTreeNode treeModel;
      if(BerechtigunsManager.ber2[34] == 1) {
         treeModel = new DefaultMutableTreeNode("Durchschnittsalter");
         noteMitglieder.add(treeModel);
      }

      if(BerechtigunsManager.ber2[35] == 1) {
         treeModel = new DefaultMutableTreeNode("Mitgliederzahlen");
         noteMitglieder.add(treeModel);
      }

      if(BerechtigunsManager.ber2[36] == 1) {
         treeModel = new DefaultMutableTreeNode("Mitglieder Dienstgrad");
         noteMitglieder.add(treeModel);
      }

      if(BerechtigunsManager.ber2[37] == 1) {
         treeModel = new DefaultMutableTreeNode("Mitglieder Funktionen (Anzahl)");
         noteMitglieder.add(treeModel);
      }

      DefaultTreeModel treeModel1 = new DefaultTreeModel(root);
      return treeModel1;
   }

   public static DefaultTreeModel CreateTreeKategorienListe() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Kategorien");
      TabelleStichwort tabStichwort = new TabelleStichwort();
      TabelleAusbildung_Kategorie tabAusbildungKategorie = new TabelleAusbildung_Kategorie();
      TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
      TabelleFahrzeug_beschreibung tabeFahrzeugbeschreibung = new TabelleFahrzeug_beschreibung();
      TabelleMitglieder_gruppe tabMitgliederGruppe = new TabelleMitglieder_gruppe();
      TabelleVeranstaltung_Kategorie tabVeranstaltungskategorie = new TabelleVeranstaltung_Kategorie();
      TabelleOrganisationen tabOrganisation = new TabelleOrganisationen();
      TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();

      try {
         String[] treeModel = Utils.listToArray(tabAusbildungKategorie.getAllKategorien());
         String[] stichwortListe = Utils.listToArray(tabStichwort.getAllStichwort());
         String[] stichwortKategorieListe = Utils.listToArray(tabKategorie.getAllEinsatzKategorie());
         String[] dienstgradListe = Utils.listToArray(tabDienstgrad.getAllDienstgradLang());
         String[] fahrzeugbeschreibungListe = Utils.listToArray(tabeFahrzeugbeschreibung.getAllFahrzeugBeschreibungen());
         String[] mitgliederGruppeListe = Utils.listToArray(tabMitgliederGruppe.getAllGruppen());
         String[] veranstaltungskategorieListe = Utils.listToArray(tabVeranstaltungskategorie.getAllKategorien());
         String[] organisationsListe = Utils.listToArray(tabOrganisation.getAllOrganisationenWithout1());
         DefaultMutableTreeNode noteVeranstaltungskategorie;
         int v;
         DefaultMutableTreeNode noteVeranstaltungskategorieListe;
         if(BerechtigunsManager.ber[47] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Ausbildungskategorie");
            root.add(noteVeranstaltungskategorie);
            logging.logInfo("Setze Root");

            for(v = 0; v < treeModel.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(treeModel[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }

         if(BerechtigunsManager.ber[0] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Dienstgrad");
            root.add(noteVeranstaltungskategorie);

            for(v = 0; v < dienstgradListe.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(dienstgradListe[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }

         if(BerechtigunsManager.ber[38] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Fahrzeugkategorie");
            root.add(noteVeranstaltungskategorie);

            for(v = 0; v < fahrzeugbeschreibungListe.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(fahrzeugbeschreibungListe[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }

         if(BerechtigunsManager.ber[46] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Mitgliedergruppe");
            root.add(noteVeranstaltungskategorie);

            for(v = 0; v < mitgliederGruppeListe.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(mitgliederGruppeListe[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }

         if(((String)runApplication.EINSTELLUNGEN.get("WeitereOrganisationen")).equals("1") && BerechtigunsManager.ber[90] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Organisationen");
            root.add(noteVeranstaltungskategorie);
            DefaultMutableTreeNode var24 = new DefaultMutableTreeNode(runApplication.EINSTELLUNGEN.get("Name"));
            noteVeranstaltungskategorie.add(var24);

            for(int var23 = 0; var23 < organisationsListe.length; ++var23) {
               DefaultMutableTreeNode noteOrganisationListe = new DefaultMutableTreeNode(organisationsListe[var23]);
               noteVeranstaltungskategorie.add(noteOrganisationListe);
            }
         }

         if(BerechtigunsManager.ber[39] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Stichwort");
            root.add(noteVeranstaltungskategorie);

            for(v = 0; v < stichwortListe.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(stichwortListe[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }

         if(BerechtigunsManager.ber[91] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Stichwortkategorie");
            root.add(noteVeranstaltungskategorie);

            for(v = 0; v < stichwortKategorieListe.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(stichwortKategorieListe[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }

         if(BerechtigunsManager.ber[40] == 1) {
            noteVeranstaltungskategorie = new DefaultMutableTreeNode("Veranstalungskategorie");
            root.add(noteVeranstaltungskategorie);

            for(v = 0; v < veranstaltungskategorieListe.length; ++v) {
               noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(veranstaltungskategorieListe[v]);
               noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
            }
         }
      } catch (SQLException var21) {
         logging.logPrintStackTrace(var21);
      }

      DefaultTreeModel var22 = new DefaultTreeModel(root);
      return var22;
   }

   public static DefaultTreeModel CreateTreeSchulungListe() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Schulung Liste");
      TabelleJahr tabJahr = new TabelleJahr();
      TabelleSchulung tabelleSchulung = new TabelleSchulung();

      try {
         String[] treeModel = Utils.listToArray(tabJahr.getAllVerfügbarenJahre());

         for(int j = 0; j < treeModel.length; ++j) {
            DefaultMutableTreeNode noteJahre = new DefaultMutableTreeNode(treeModel[j]);
            root.add(noteJahre);
            String[] schulungListe = Utils.listToArray(tabelleSchulung.getAlleSchulungenEinesJahres(Integer.parseInt(treeModel[j])));

            for(int s = 0; s < schulungListe.length; ++s) {
               DefaultMutableTreeNode note = new DefaultMutableTreeNode(schulungListe[s]);
               noteJahre.add(note);
            }
         }
      } catch (SQLException var9) {
         logging.logPrintStackTrace(var9);
      }

      DefaultTreeModel var10 = new DefaultTreeModel(root);
      return var10;
   }

   public static DefaultTreeModel CreateTreeStraßenHydranten() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Einsatzgebiet");
      TabelleStrassen tabStraßen = new TabelleStrassen();

      try {
         DefaultMutableTreeNode treeModel = new DefaultMutableTreeNode("Straßen");
         root.add(treeModel);
         String[] straßenListe = Utils.listToArray(tabStraßen.getStraßenListe());

         for(int i = 0; i < straßenListe.length; ++i) {
            DefaultMutableTreeNode noteStraßen = new DefaultMutableTreeNode(straßenListe[i]);
            treeModel.add(noteStraßen);
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      DefaultTreeModel var7 = new DefaultTreeModel(root);
      return var7;
   }
}
