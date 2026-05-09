/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities;

import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleBriefe;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeug_beschreibung;
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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
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
            DefaultMutableTreeNode note;
            String[] artikelListe = Utils.listToArray(tabArtikel.getAllArtikel());
            String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
            String[] mitgliederListe = Utils.listToArray(tabMitglied.getAllMitgliederFromDataBase());
            String[] lagerListe = Utils.listToArray(tabLager.getAllLager());
            int a = 0;
            while (a < artikelListe.length) {
                note = new DefaultMutableTreeNode(artikelListe[a]);
                noteA.add(note);
                ++a;
            }
            int l = 0;
            while (l < lagerListe.length) {
                note = new DefaultMutableTreeNode(lagerListe[l]);
                noteL.add(note);
                ++l;
            }
            int f = 0;
            while (f < fahrzeugListe.length) {
                note = new DefaultMutableTreeNode(fahrzeugListe[f]);
                noteF.add(note);
                ++f;
            }
            int m = 0;
            while (m < mitgliederListe.length) {
                note = new DefaultMutableTreeNode(mitgliederListe[m]);
                noteM.add(note);
                ++m;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeMitgliederListe(String suche) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mitglieder Liste");
        TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        try {
            String[] mGruppe = Utils.listToArray(tabGruppe.getAllGruppen());
            int g = 0;
            while (g < mGruppe.length) {
                DefaultMutableTreeNode noteGruppe = new DefaultMutableTreeNode(mGruppe[g]);
                root.add(noteGruppe);
                String[] mitgliederListe = null;
                mitgliederListe = suche == null ? Utils.listToArray(tabMitglieder.getMitgliederEinerGruppe(g + 1)) : Utils.listToArray(tabMitglieder.getMitgliederEinerGruppeForSearch(g + 1, suche));
                int m = 0;
                while (m < mitgliederListe.length) {
                    DefaultMutableTreeNode note = new DefaultMutableTreeNode(mitgliederListe[m]);
                    noteGruppe.add(note);
                    ++m;
                }
                ++g;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeFahrzeugListe() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Fahrzeug Liste");
        TabelleFahrzeug_beschreibung tabBeschreibung = new TabelleFahrzeug_beschreibung();
        TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
        try {
            String[] fBeschreibung = Utils.listToArray(tabBeschreibung.getAllFahrzeugBeschreibungen());
            int[] fBeschreibungID = Utils.listToIntArray(tabBeschreibung.getAllFahrzeugBeschreibungenID());
            int b = 0;
            while (b < fBeschreibung.length) {
                DefaultMutableTreeNode noteBeschreibung = new DefaultMutableTreeNode(fBeschreibung[b]);
                root.add(noteBeschreibung);
                String[] fahrzeugListe = Utils.listToArray(tabFahrzeuge.getFahrzeugeByBeschreibungID(fBeschreibungID[b]));
                int f = 0;
                while (f < fahrzeugListe.length) {
                    DefaultMutableTreeNode note = new DefaultMutableTreeNode(fahrzeugListe[f]);
                    noteBeschreibung.add(note);
                    ++f;
                }
                ++b;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeDokumentenListe() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Ordner Liste");
        TabelleJahr tabJahr = new TabelleJahr();
        if (runApplication.EINSTELLUNGEN.get("abrechnungModul").equals("1") && BerechtigunsManager.ber2[39] == 1) {
            DefaultMutableTreeNode noteAbrechnungen = new DefaultMutableTreeNode("Abrechnung");
            root.add(noteAbrechnungen);
        }
        if (BerechtigunsManager.ber2[40] == 1) {
            DefaultMutableTreeNode noteAusbildungsunterlagen = new DefaultMutableTreeNode("Ausbildungsunterlagen");
            root.add(noteAusbildungsunterlagen);
        }
        if (BerechtigunsManager.ber2[41] == 1) {
            DefaultMutableTreeNode noteBestandsliste = new DefaultMutableTreeNode("Bestandsliste");
            root.add(noteBestandsliste);
        }
        if (BerechtigunsManager.ber2[42] == 1) {
            DefaultMutableTreeNode noteEigeneDateien = new DefaultMutableTreeNode("Eigene Dateien");
            root.add(noteEigeneDateien);
        }
        if (BerechtigunsManager.ber2[51] == 1) {
            DefaultMutableTreeNode noteAtemschutz = new DefaultMutableTreeNode("Atemschutz");
            root.add(noteAtemschutz);
        }
        try {
            String[] jahre = Utils.listToArray(tabJahr.getAllVerf\u00fcgbarenJahre());
            int j = 0;
            while (j < jahre.length) {
                DefaultMutableTreeNode noteJahre = new DefaultMutableTreeNode(jahre[j]);
                root.add(noteJahre);
                if (BerechtigunsManager.ber2[50] == 1) {
                    DefaultMutableTreeNode noteBerichte = new DefaultMutableTreeNode("Berichte");
                    noteJahre.add(noteBerichte);
                }
                if (BerechtigunsManager.ber2[49] == 1) {
                    DefaultMutableTreeNode noteBeteiligung = new DefaultMutableTreeNode("Beteiligungs\u00fcbersicht");
                    noteJahre.add(noteBeteiligung);
                }
                if (BerechtigunsManager.ber2[48] == 1) {
                    DefaultMutableTreeNode noteBriefe = new DefaultMutableTreeNode("Briefe");
                    noteJahre.add(noteBriefe);
                }
                if (BerechtigunsManager.ber2[47] == 1) {
                    DefaultMutableTreeNode noteEinsatzberichte = new DefaultMutableTreeNode("Einsatzberichte");
                    noteJahre.add(noteEinsatzberichte);
                }
                if (BerechtigunsManager.ber2[46] == 1) {
                    DefaultMutableTreeNode noteFahrzeugeinteilung = new DefaultMutableTreeNode("Fahrzeugeinteilung");
                    noteJahre.add(noteFahrzeugeinteilung);
                }
                if (BerechtigunsManager.ber2[45] == 1) {
                    DefaultMutableTreeNode noteLehrgangsmeldungen = new DefaultMutableTreeNode("Lehrgangsmeldungen");
                    noteJahre.add(noteLehrgangsmeldungen);
                }
                if (BerechtigunsManager.ber2[44] == 1) {
                    DefaultMutableTreeNode noteM\u00e4ngel = new DefaultMutableTreeNode("M\u00e4ngelmeldungen");
                    noteJahre.add(noteM\u00e4ngel);
                }
                if (BerechtigunsManager.ber2[43] == 1) {
                    DefaultMutableTreeNode noteVerdienstausfall = new DefaultMutableTreeNode("Verdienstausfallbescheinigung");
                    noteJahre.add(noteVerdienstausfall);
                }
                if (BerechtigunsManager.ber[89] == 1) {
                    DefaultMutableTreeNode noteProtokoll = new DefaultMutableTreeNode("Protokoll");
                    noteJahre.add(noteProtokoll);
                }
                if (runApplication.EINSTELLUNGEN.get("Schichtplaner").equals("1")) {
                    DefaultMutableTreeNode noteOdner = new DefaultMutableTreeNode("Schichten");
                    noteJahre.add(noteOdner);
                }
                ++j;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeMaengelListe(String mandantID) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("M\u00e4ngelmeldungen");
        TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
        TabelleJahr tabJahr = new TabelleJahr();
        try {
            String[] jahre = Utils.listToArray(tabJahr.getAllVerf\u00fcgbarenJahre());
            int j = 0;
            while (j < jahre.length) {
                DefaultMutableTreeNode noteJahre = new DefaultMutableTreeNode(jahre[j]);
                root.add(noteJahre);
                int aktellesJahr = Integer.parseInt(jahre[j]);
                String[] maengelListeOffen = Utils.listToArray(tabMangel.getMaengelmeldungWithStatus(0, aktellesJahr, mandantID));
                String[] maengelListeGeschlossen = Utils.listToArray(tabMangel.getMaengelmeldungWithStatus(1, aktellesJahr, mandantID));
                DefaultMutableTreeNode noteOffene = new DefaultMutableTreeNode("Offene M\u00e4ngelmeldungen");
                DefaultMutableTreeNode noteGeschlossene = new DefaultMutableTreeNode("Geschlossene M\u00e4ngelmeldungen");
                noteJahre.add(noteOffene);
                noteJahre.add(noteGeschlossene);
                int o = 0;
                while (o < maengelListeOffen.length) {
                    DefaultMutableTreeNode noteGruppe1 = new DefaultMutableTreeNode(maengelListeOffen[o]);
                    noteOffene.add(noteGruppe1);
                    ++o;
                }
                int g = 0;
                while (g < maengelListeGeschlossen.length) {
                    DefaultMutableTreeNode noteGruppe2 = new DefaultMutableTreeNode(maengelListeGeschlossen[g]);
                    noteGeschlossene.add(noteGruppe2);
                    ++g;
                }
                ++j;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeBriefeTemplates() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vorlagen");
        TabelleBriefe tabBrief = new TabelleBriefe();
        try {
            String[] templates = Utils.listToArray(tabBrief.getTemplates());
            int t = 0;
            while (t < templates.length) {
                DefaultMutableTreeNode noteTemplate = new DefaultMutableTreeNode(templates[t]);
                root.add(noteTemplate);
                ++t;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeJahresberichteTemplates() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Vorlagen");
        TabelleJahresbericht tabBericht = new TabelleJahresbericht();
        try {
            String[] templates = Utils.listToArray(tabBericht.getAllTitle());
            int t = 0;
            while (t < templates.length) {
                DefaultMutableTreeNode noteTemplate = new DefaultMutableTreeNode(templates[t]);
                root.add(noteTemplate);
                ++t;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeAbrechnung() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Liste");
        TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
        TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
        TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        try {
            DefaultMutableTreeNode noteBilanz = new DefaultMutableTreeNode("Kontobilanz");
            root.add(noteBilanz);
            String[] liste = Utils.listToArray(tabKonto.getAllKontos());
            int t = 0;
            while (t < liste.length) {
                DefaultMutableTreeNode noteKontos = new DefaultMutableTreeNode(liste[t]);
                noteBilanz.add(noteKontos);
                ++t;
            }
            DefaultMutableTreeNode noteKonten = new DefaultMutableTreeNode("Konten");
            root.add(noteKonten);
            int t2 = 0;
            while (t2 < liste.length) {
                DefaultMutableTreeNode noteKontos = new DefaultMutableTreeNode(liste[t2]);
                noteKonten.add(noteKontos);
                ++t2;
            }
            DefaultMutableTreeNode noteAbrechnungen = new DefaultMutableTreeNode("Abrechnungen");
            root.add(noteAbrechnungen);
            String[] listeAbrechnungen = Utils.listToArray(tabAbrechnung.getAllAbrechnungID());
            int t3 = 0;
            while (t3 < listeAbrechnungen.length) {
                DefaultMutableTreeNode noteAbrechnungenID = new DefaultMutableTreeNode(listeAbrechnungen[t3]);
                noteAbrechnungen.add(noteAbrechnungenID);
                ++t3;
            }
            DefaultMutableTreeNode noteMitglieder = new DefaultMutableTreeNode("Mitglieder");
            root.add(noteMitglieder);
            String[] mGruppe = Utils.listToArray(tabGruppe.getAllGruppen());
            int g = 0;
            while (g < mGruppe.length) {
                DefaultMutableTreeNode noteGruppe = new DefaultMutableTreeNode(mGruppe[g]);
                noteMitglieder.add(noteGruppe);
                String[] mitgliederListe = null;
                mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederEinerGruppe(g + 1));
                int m = 0;
                while (m < mitgliederListe.length) {
                    DefaultMutableTreeNode note = new DefaultMutableTreeNode(mitgliederListe[m]);
                    noteGruppe.add(note);
                    ++m;
                }
                ++g;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    public static DefaultTreeModel CreateTreeStatistik() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Statistik");
        DefaultMutableTreeNode noteAnwesenheit = new DefaultMutableTreeNode("Anwesenheit");
        root.add(noteAnwesenheit);
        if (BerechtigunsManager.ber[21] == 1) {
            DefaultMutableTreeNode noteEinsatzbeteiligung = new DefaultMutableTreeNode("Einsatz");
            noteAnwesenheit.add(noteEinsatzbeteiligung);
            DefaultMutableTreeNode noteEinsatzbeteiligungDurchschnitt = new DefaultMutableTreeNode("Durchschnittliche Einsatzbeteiligung");
            noteAnwesenheit.add(noteEinsatzbeteiligungDurchschnitt);
        }
        if (BerechtigunsManager.ber[22] == 1) {
            DefaultMutableTreeNode noteDienstBeteiligung = new DefaultMutableTreeNode("Dienst");
            noteAnwesenheit.add(noteDienstBeteiligung);
            DefaultMutableTreeNode noteDienstbeteiligungDurchschnitt = new DefaultMutableTreeNode("Durchschnittliche Dienstbeteiligung");
            noteAnwesenheit.add(noteDienstbeteiligungDurchschnitt);
        }
        if (BerechtigunsManager.ber[23] == 1) {
            DefaultMutableTreeNode noteBSWBeteiligung = new DefaultMutableTreeNode("BSW");
            noteAnwesenheit.add(noteBSWBeteiligung);
        }
        if (BerechtigunsManager.ber2[26] == 1) {
            DefaultMutableTreeNode noteSonstigeBeteiligung = new DefaultMutableTreeNode("Sonstige Veranstaltungen");
            noteAnwesenheit.add(noteSonstigeBeteiligung);
        }
        if (BerechtigunsManager.ber[20] == 1) {
            DefaultMutableTreeNode noteAnwesenheitGesamt = new DefaultMutableTreeNode("Anwesenheit Gesamt");
            noteAnwesenheit.add(noteAnwesenheitGesamt);
            DefaultMutableTreeNode noteAnwesenheitGesamtInProzent = new DefaultMutableTreeNode("Anwesenheit Gesamt in %");
            noteAnwesenheit.add(noteAnwesenheitGesamtInProzent);
        }
        if (BerechtigunsManager.ber[61] == 1) {
            DefaultMutableTreeNode noteBeteiligungsdauer = new DefaultMutableTreeNode("Beteiligungsdauer");
            noteAnwesenheit.add(noteBeteiligungsdauer);
        }
        if (BerechtigunsManager.ber[34] == 1) {
            DefaultMutableTreeNode noteBeteiligungBei = new DefaultMutableTreeNode("Beteiligung bei...");
            noteAnwesenheit.add(noteBeteiligungBei);
        }
        if (BerechtigunsManager.ber2[27] == 1) {
            DefaultMutableTreeNode noteVerfuegbarkeit = new DefaultMutableTreeNode("Verf\u00fcgbarkeit Einsatz");
            noteAnwesenheit.add(noteVerfuegbarkeit);
        }
        DefaultMutableTreeNode noteAbwesenheit = new DefaultMutableTreeNode("Abwesenheit");
        root.add(noteAbwesenheit);
        if (BerechtigunsManager.ber[24] == 1) {
            DefaultMutableTreeNode noteAbwesenheitsstatistik = new DefaultMutableTreeNode("Abwesenheitsstatistik");
            noteAbwesenheit.add(noteAbwesenheitsstatistik);
            DefaultMutableTreeNode noteAnAbwesenheitsstatistikMitglied = new DefaultMutableTreeNode("An- / Abwesenheitsstatistik");
            noteAbwesenheit.add(noteAnAbwesenheitsstatistikMitglied);
        }
        DefaultMutableTreeNode noteAusbildung = new DefaultMutableTreeNode("Ausbildung");
        root.add(noteAusbildung);
        if (BerechtigunsManager.ber[35] == 1) {
            DefaultMutableTreeNode noteAusbildungstatistik = new DefaultMutableTreeNode("Ausbildungsstatistik");
            noteAusbildung.add(noteAusbildungstatistik);
            DefaultMutableTreeNode noteAusbilderstatistik = new DefaultMutableTreeNode("Ausbilder Statistik");
            noteAusbildung.add(noteAusbilderstatistik);
        }
        DefaultMutableTreeNode noteEinsatz = new DefaultMutableTreeNode("Einsatzstatistik");
        root.add(noteEinsatz);
        if (BerechtigunsManager.ber[25] == 1) {
            DefaultMutableTreeNode noteEinsatzArt = new DefaultMutableTreeNode("Einsatzart");
            noteEinsatz.add(noteEinsatzArt);
            DefaultMutableTreeNode noteStichwort = new DefaultMutableTreeNode("Stichwort Statistik");
            noteEinsatz.add(noteStichwort);
        }
        if (BerechtigunsManager.ber[26] == 1) {
            DefaultMutableTreeNode noteAusr\u00fcckedauer = new DefaultMutableTreeNode("Ausr\u00fcckezeiten");
            noteEinsatz.add(noteAusr\u00fcckedauer);
        }
        if (BerechtigunsManager.ber[37] == 1 && runApplication.EINSTELLUNGEN.get("feldEintreffenAusblenden").equals("0")) {
            DefaultMutableTreeNode noteDauer = new DefaultMutableTreeNode("Alarmfahrtdauer");
            noteEinsatz.add(noteDauer);
        }
        if (BerechtigunsManager.ber[36] == 1) {
            DefaultMutableTreeNode noteFahrzeuge = new DefaultMutableTreeNode("Fahrzeuge im Einsatz");
            noteEinsatz.add(noteFahrzeuge);
        }
        if (BerechtigunsManager.ber[33] == 1) {
            DefaultMutableTreeNode noteFehlalarme = new DefaultMutableTreeNode("Fehlalarme");
            noteEinsatz.add(noteFehlalarme);
        }
        if (BerechtigunsManager.ber[27] == 1) {
            DefaultMutableTreeNode noteEinsatzDauer = new DefaultMutableTreeNode("Durchschnittliche Einsatzdauer");
            noteEinsatz.add(noteEinsatzDauer);
        }
        if (BerechtigunsManager.ber[19] == 1) {
            DefaultMutableTreeNode noteProMonat = new DefaultMutableTreeNode("Einsatz pro Monat");
            noteEinsatz.add(noteProMonat);
        }
        if (BerechtigunsManager.ber[31] == 1) {
            DefaultMutableTreeNode noteProWochentag = new DefaultMutableTreeNode("Einsatz Pro Wochentag");
            noteEinsatz.add(noteProWochentag);
        }
        if (BerechtigunsManager.ber[30] == 1) {
            DefaultMutableTreeNode noteProStunde = new DefaultMutableTreeNode("Einsatz pro Stunde");
            noteEinsatz.add(noteProStunde);
        }
        if (runApplication.EINSTELLUNGEN.get("feldStadtteilAusblenden").equals("0") && BerechtigunsManager.ber2[28] == 1) {
            DefaultMutableTreeNode noteStadtteil = new DefaultMutableTreeNode("Stadtteil Statistik");
            noteEinsatz.add(noteStadtteil);
        }
        if (BerechtigunsManager.ber2[29] == 1) {
            DefaultMutableTreeNode noteSchutzziel = new DefaultMutableTreeNode("Schutzziel Statistik");
            noteEinsatz.add(noteSchutzziel);
        }
        if (BerechtigunsManager.ber2[30] == 1) {
            DefaultMutableTreeNode noteTagNacht = new DefaultMutableTreeNode("Tag / Nacht Eins\u00e4tze");
            noteEinsatz.add(noteTagNacht);
        }
        if (BerechtigunsManager.ber2[31] == 1) {
            DefaultMutableTreeNode noteFahrzeugbelegung = new DefaultMutableTreeNode("Fahrzeugbelegung");
            noteEinsatz.add(noteFahrzeugbelegung);
        }
        DefaultMutableTreeNode noteMannstaunden = new DefaultMutableTreeNode("Mannstunden");
        root.add(noteMannstaunden);
        if (BerechtigunsManager.ber[28] == 1) {
            DefaultMutableTreeNode noteMannstaundenE = new DefaultMutableTreeNode("Einsatz Mannstunden");
            noteMannstaunden.add(noteMannstaundenE);
            DefaultMutableTreeNode noteMannstaundenEpM = new DefaultMutableTreeNode("Einsatz Mannstunden pro Monat");
            noteMannstaunden.add(noteMannstaundenEpM);
        }
        if (BerechtigunsManager.ber[32] == 1) {
            DefaultMutableTreeNode noteMannstaundenB = new DefaultMutableTreeNode("BSW Mannstunden");
            noteMannstaunden.add(noteMannstaundenB);
            DefaultMutableTreeNode noteMannstaundenBpM = new DefaultMutableTreeNode("BSW Mannstunden pro Monat");
            noteMannstaunden.add(noteMannstaundenBpM);
        }
        if (BerechtigunsManager.ber[53] == 1) {
            DefaultMutableTreeNode noteMannstaundenS = new DefaultMutableTreeNode("Sonstige Mannstunden");
            noteMannstaunden.add(noteMannstaundenS);
        }
        DefaultMutableTreeNode noteAtemschutz = new DefaultMutableTreeNode("Atemschutz");
        root.add(noteAtemschutz);
        if (BerechtigunsManager.ber2[32] == 1) {
            DefaultMutableTreeNode noteAtemschutzstatistik = new DefaultMutableTreeNode("Atemschutzstatistik");
            noteAtemschutz.add(noteAtemschutzstatistik);
        }
        DefaultMutableTreeNode noteVeranstaltung = new DefaultMutableTreeNode("Veranstaltung");
        root.add(noteVeranstaltung);
        if (BerechtigunsManager.ber2[33] == 1) {
            DefaultMutableTreeNode noteVeranstaltungsstatistik = new DefaultMutableTreeNode("Veranstaltungsz\u00e4hlung");
            noteVeranstaltung.add(noteVeranstaltungsstatistik);
        }
        DefaultMutableTreeNode noteMitglieder = new DefaultMutableTreeNode("Mitglieder");
        root.add(noteMitglieder);
        if (BerechtigunsManager.ber2[34] == 1) {
            DefaultMutableTreeNode noteAlter = new DefaultMutableTreeNode("Durchschnittsalter");
            noteMitglieder.add(noteAlter);
        }
        if (BerechtigunsManager.ber2[35] == 1) {
            DefaultMutableTreeNode noteMitgliederZahlen = new DefaultMutableTreeNode("Mitgliederzahlen");
            noteMitglieder.add(noteMitgliederZahlen);
        }
        if (BerechtigunsManager.ber2[36] == 1) {
            DefaultMutableTreeNode noteDienstgardZahlen = new DefaultMutableTreeNode("Mitglieder Dienstgrad");
            noteMitglieder.add(noteDienstgardZahlen);
        }
        if (BerechtigunsManager.ber2[37] == 1) {
            DefaultMutableTreeNode noteMitgliederFunktionen = new DefaultMutableTreeNode("Mitglieder Funktionen (Anzahl)");
            noteMitglieder.add(noteMitgliederFunktionen);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
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
            String[] ausbildungListe = Utils.listToArray(tabAusbildungKategorie.getAllKategorien());
            String[] stichwortListe = Utils.listToArray(tabStichwort.getAllStichwort());
            String[] stichwortKategorieListe = Utils.listToArray(tabKategorie.getAllEinsatzKategorie());
            String[] dienstgradListe = Utils.listToArray(tabDienstgrad.getAllDienstgradLang());
            String[] fahrzeugbeschreibungListe = Utils.listToArray(tabeFahrzeugbeschreibung.getAllFahrzeugBeschreibungen());
            String[] mitgliederGruppeListe = Utils.listToArray(tabMitgliederGruppe.getAllGruppen());
            String[] veranstaltungskategorieListe = Utils.listToArray(tabVeranstaltungskategorie.getAllKategorien());
            String[] organisationsListe = Utils.listToArray(tabOrganisation.getAllOrganisationenWithout1());
            if (BerechtigunsManager.ber[47] == 1) {
                DefaultMutableTreeNode noteAusbildung = new DefaultMutableTreeNode("Ausbildungskategorie");
                root.add(noteAusbildung);
                logging.logInfo((Object)"Setze Root");
                int a = 0;
                while (a < ausbildungListe.length) {
                    DefaultMutableTreeNode noteAusbildungListe = new DefaultMutableTreeNode(ausbildungListe[a]);
                    noteAusbildung.add(noteAusbildungListe);
                    ++a;
                }
            }
            if (BerechtigunsManager.ber[0] == 1) {
                DefaultMutableTreeNode noteDienstgrad = new DefaultMutableTreeNode("Dienstgrad");
                root.add(noteDienstgrad);
                int d = 0;
                while (d < dienstgradListe.length) {
                    DefaultMutableTreeNode noteDienstgradListe = new DefaultMutableTreeNode(dienstgradListe[d]);
                    noteDienstgrad.add(noteDienstgradListe);
                    ++d;
                }
            }
            if (BerechtigunsManager.ber[38] == 1) {
                DefaultMutableTreeNode noteFahrzeug = new DefaultMutableTreeNode("Fahrzeugkategorie");
                root.add(noteFahrzeug);
                int f = 0;
                while (f < fahrzeugbeschreibungListe.length) {
                    DefaultMutableTreeNode noteFahrzeugBeschreibungListe = new DefaultMutableTreeNode(fahrzeugbeschreibungListe[f]);
                    noteFahrzeug.add(noteFahrzeugBeschreibungListe);
                    ++f;
                }
            }
            if (BerechtigunsManager.ber[46] == 1) {
                DefaultMutableTreeNode noteMitgliederGruppe = new DefaultMutableTreeNode("Mitgliedergruppe");
                root.add(noteMitgliederGruppe);
                int m = 0;
                while (m < mitgliederGruppeListe.length) {
                    DefaultMutableTreeNode noteMitgliederGruppeListe = new DefaultMutableTreeNode(mitgliederGruppeListe[m]);
                    noteMitgliederGruppe.add(noteMitgliederGruppeListe);
                    ++m;
                }
            }
            if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && BerechtigunsManager.ber[90] == 1) {
                DefaultMutableTreeNode noteOrganisationen = new DefaultMutableTreeNode("Organisationen");
                root.add(noteOrganisationen);
                DefaultMutableTreeNode noteMeineOrganisation = new DefaultMutableTreeNode(runApplication.EINSTELLUNGEN.get("Name"));
                noteOrganisationen.add(noteMeineOrganisation);
                int o = 0;
                while (o < organisationsListe.length) {
                    DefaultMutableTreeNode noteOrganisationListe = new DefaultMutableTreeNode(organisationsListe[o]);
                    noteOrganisationen.add(noteOrganisationListe);
                    ++o;
                }
            }
            if (BerechtigunsManager.ber[39] == 1) {
                DefaultMutableTreeNode noteStichwort = new DefaultMutableTreeNode("Stichwort");
                root.add(noteStichwort);
                int s = 0;
                while (s < stichwortListe.length) {
                    DefaultMutableTreeNode noteStichwortListe = new DefaultMutableTreeNode(stichwortListe[s]);
                    noteStichwort.add(noteStichwortListe);
                    ++s;
                }
            }
            if (BerechtigunsManager.ber[91] == 1) {
                DefaultMutableTreeNode noteStichwortKategorie = new DefaultMutableTreeNode("Stichwortkategorie");
                root.add(noteStichwortKategorie);
                int sk = 0;
                while (sk < stichwortKategorieListe.length) {
                    DefaultMutableTreeNode noteStichwortKategorieListe = new DefaultMutableTreeNode(stichwortKategorieListe[sk]);
                    noteStichwortKategorie.add(noteStichwortKategorieListe);
                    ++sk;
                }
            }
            if (BerechtigunsManager.ber[40] == 1) {
                DefaultMutableTreeNode noteVeranstaltungskategorie = new DefaultMutableTreeNode("Veranstalungskategorie");
                root.add(noteVeranstaltungskategorie);
                int v = 0;
                while (v < veranstaltungskategorieListe.length) {
                    DefaultMutableTreeNode noteVeranstaltungskategorieListe = new DefaultMutableTreeNode(veranstaltungskategorieListe[v]);
                    noteVeranstaltungskategorie.add(noteVeranstaltungskategorieListe);
                    ++v;
                }
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }
}

