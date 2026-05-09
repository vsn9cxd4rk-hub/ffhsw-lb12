/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package steuerung;

import data.DatenbankZugriff;
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
import steuerung.prozesse.AtemschutzpassEintragAnzeigen;
import steuerung.prozesse.AtemschutzpassListeAnzeigen;
import steuerung.prozesse.AusbildungAnzeigen;
import steuerung.prozesse.AusbildungKategorieAnlegenAnzeigen;
import steuerung.prozesse.AusbildungsinhalteTauschenAnzeigen;
import steuerung.prozesse.AusbildungsplanAnzeigen;
import steuerung.prozesse.BankverbindungAnzeigen;
import steuerung.prozesse.Bef\u00f6rderungAnzeigen;
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
import steuerung.prozesse.FahrtenbuchAnzeigen;
import steuerung.prozesse.FahrtenbuchListeAnzeigen;
import steuerung.prozesse.FahrzeugAnlegenAnzeigen;
import steuerung.prozesse.FahrzeugBelegungAnzeigen;
import steuerung.prozesse.FahrzeugEinteilungAnzeigen;
import steuerung.prozesse.FahrzeugGruppeAnzeigen;
import steuerung.prozesse.FahrzeugUntersuchungAnzeigen;
import steuerung.prozesse.FahrzeugakteAnzeigen;
import steuerung.prozesse.FahrzeugeinteilungNachtragenAnzeigen;
import steuerung.prozesse.Ger\u00e4tepr\u00fcfungAnzeigen;
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
import steuerung.prozesse.M\u00e4ngelmeldungAnzeigen;
import steuerung.prozesse.M\u00e4ngelmeldungBearbeitenAnzeigen;
import steuerung.prozesse.M\u00e4ngelmeldungKommentarAnzeigen;
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
import steuerung.prozesse.karte.HydrantEintargenAnzeigen;
import steuerung.prozesse.karte.ImportAnzeigen;
import steuerung.prozesse.karte.KarteAnzeigen;
import steuerung.prozesse.karte.Stra\u00dfenEintargenAnzeigen;
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
import steuerung.prozesse.utils.TerminDisplayAnzeigen;
import steuerung.unwetterwarnung.UnwetterwarnungAnzeigen;
import utilities.Konstante;
import utilities.logbuchEingabe;

public class Steuerung {
    private static Status status;

    public static void steuerung() {
        Anzeige anzeige = null;
        if (!Steuerung.isStatus(Status.ENDE)) {
            if (Steuerung.isStatus(Status.HAUPTPROGRAMM)) {
                anzeige = new HauptprogrammAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDER_ANLEGEN)) {
                anzeige = new MitgliederAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHREZUG_ANLEGEN)) {
                anzeige = new FahrzeugAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.STICHWORT_ANLEGEN)) {
                anzeige = new StichwortAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.EINSATZ_EINTRAGEN)) {
                anzeige = new EinsatzEintragenAnzeigen();
            } else if (Steuerung.isStatus(Status.VERANSTALTUNG_ANLEGEN)) {
                anzeige = new VeranstaltungAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.VERANSTALTUNG_EDITIEREN)) {
                anzeige = new VeranstaltungEditierenAnzeigen();
            } else if (Steuerung.isStatus(Status.ANWESENHEIT_EINTRAGEN)) {
                anzeige = new AnwesenheitEintragenAnzeigen();
            } else if (Steuerung.isStatus(Status.VERANSTALTUNG_KATEGORIE_ANLEGEN)) {
                anzeige = new VeranstaltungKategorieAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRZEUGEINTEILUNG)) {
                anzeige = new FahrzeugEinteilungAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDER_LISTE)) {
                anzeige = new MitgliederListeAnzeigen();
            } else if (Steuerung.isStatus(Status.EINSATZ_LISTE)) {
                anzeige = new EinsatzListeAnzeigen();
            } else if (Steuerung.isStatus(Status.BSW_EINTARGEN)) {
                anzeige = new BrandsicherheitswacheEintragenAnzeigen();
            } else if (Steuerung.isStatus(Status.BSW_LISTE)) {
                anzeige = new BrandsicherheitswachenListeAnzeigen();
            } else if (Steuerung.isStatus(Status.LEHRGANG_LISTE)) {
                anzeige = new LehrgangListeAnzeigen();
            } else if (Steuerung.isStatus(Status.ANWESENHEIT_LISTE)) {
                anzeige = new AnwesenheitListeAnzeigen();
            } else if (Steuerung.isStatus(Status.GRUNDKONFIGURATION)) {
                anzeige = new GrundkonfigurationAnzeigen();
            } else if (Steuerung.isStatus(Status.GRUNDKONFIGURATION_JWS)) {
                anzeige = new GrundkonfigurationJWSAnzeigen();
            } else if (Steuerung.isStatus(Status.PROZESSBAR)) {
                anzeige = new ProzessbarAnzeigen();
            } else if (Steuerung.isStatus(Status.ARBEITGEBER_ANLEGEN)) {
                anzeige = new ArbeitgeberAnzeigen();
            } else if (Steuerung.isStatus(Status.ARBEITGEBER_LISTE)) {
                anzeige = new ArbeitgeberListeAnzeigen();
            } else if (Steuerung.isStatus(Status.ANGEHORIGE_ANLEGEN)) {
                anzeige = new AngehoerigeAnzeigen();
            } else if (Steuerung.isStatus(Status.ANGEHOERIGEN_LISTE)) {
                anzeige = new AngehoerigeListeAnzeigen();
            } else if (Steuerung.isStatus(Status.MITLIED_AUSSER_DIENST)) {
                anzeige = new MitgliedAusserDienstAnzeigen();
            } else if (Steuerung.isStatus(Status.STRA\u00dfE_EINTRAGEN)) {
                anzeige = new Stra\u00dfenEintargenAnzeigen();
            } else if (Steuerung.isStatus(Status.HYDRANT_EINTARGEN)) {
                anzeige = new HydrantEintargenAnzeigen();
            } else if (Steuerung.isStatus(Status.IMPORT)) {
                anzeige = new ImportAnzeigen();
            } else if (Steuerung.isStatus(Status.ANMELDUNG)) {
                anzeige = new AnmeldungAnzeigen();
            } else if (Steuerung.isStatus(Status.KARTE)) {
                anzeige = new KarteAnzeigen();
            } else if (Steuerung.isStatus(Status.JAHRESBERICHT)) {
                anzeige = new JahresberichtAnzeigen();
            } else if (Steuerung.isStatus(Status.ABWESENHEITSGRUND_ANLEGEN)) {
                anzeige = new AbwesenheitsgrundAnzeigen();
            } else if (Steuerung.isStatus(Status.Abwesenheit)) {
                anzeige = new AbwesenheitAnzeigen();
            } else if (Steuerung.isStatus(Status.EINSTELLUNGEN)) {
                anzeige = new EinstellungAnzeigen();
            } else if (Steuerung.isStatus(Status.DOKUMENTENEXPLORER)) {
                anzeige = new DokumentenexplorerAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDER_GRUPPE_ANLEGEN)) {
                anzeige = new MitgliederGruppeAnzeigen();
            } else if (Steuerung.isStatus(Status.BRIEF)) {
                anzeige = new BriefAnzeigen();
            } else if (Steuerung.isStatus(Status.EINSATZ_BERICHT)) {
                anzeige = new EinsatzBerichtAnzeigen();
            } else if (Steuerung.isStatus(Status.VERDIENSTAUSFALL)) {
                anzeige = new VerdienstausfallAnzeigen();
            } else if (Steuerung.isStatus(Status.UNTERSUCHUNG_ANLEGEN)) {
                anzeige = new MitgliederUntersuchungAnzeigen();
            } else if (Steuerung.isStatus(Status.UNTERSUCHUNG_LISTE)) {
                anzeige = new MitgliederUntersuchungListeAnzeigen();
            } else if (Steuerung.isStatus(Status.AUSBILDUNG_KATEGORIE)) {
                anzeige = new AusbildungKategorieAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.AUSBILDUNGINHALT_EINTRAGEN)) {
                anzeige = new AusbildungAnzeigen();
            } else if (Steuerung.isStatus(Status.BETEILIGUNG_UEBERSICHT_LISTE)) {
                anzeige = new BeteiligungUebersichtListeAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRZUEG_UNTERSUCHUNG)) {
                anzeige = new FahrzeugUntersuchungAnzeigen();
            } else if (Steuerung.isStatus(Status.BANKVERBINDUNG_ANLEGEN)) {
                anzeige = new BankverbindungAnzeigen();
            } else if (Steuerung.isStatus(Status.GERAETEPRUEFUNG)) {
                anzeige = new Ger\u00e4tepr\u00fcfungAnzeigen();
            } else if (Steuerung.isStatus(Status.BANKVERBINDUNG_LISTE)) {
                anzeige = new MitgliederBankverbindungListeAnzeigen();
            } else if (Steuerung.isStatus(Status.MAENGELMELDUNG)) {
                anzeige = new M\u00e4ngelmeldungAnzeigen();
            } else if (Steuerung.isStatus(Status.VERANSTALTUNG_LISTE)) {
                anzeige = new VeranstaltungListeAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRZEUGEINTEILUNG_NACHTRAGEN)) {
                anzeige = new FahrzeugeinteilungNachtragenAnzeigen();
            } else if (Steuerung.isStatus(Status.BERECHTIGUNG)) {
                anzeige = new BerechtigungAnzeigen();
            } else if (Steuerung.isStatus(Status.BENUTZER_ANLEGEN)) {
                anzeige = new BenutzerAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.LEHRGANGSMELDUNG)) {
                anzeige = new LehrgangsmeldungAnzeigen();
            } else if (Steuerung.isStatus(Status.NUTZUNGSHINWEISE)) {
                anzeige = new LizenzAnzeigen();
            } else if (Steuerung.isStatus(Status.VERDIENSTAUSFALL_ZEITENANPASSEN)) {
                anzeige = new Verdienstausfall_ZeitenAnpassenAnzeigen();
            } else if (Steuerung.isStatus(Status.ANREDE_ANLEGEN)) {
                anzeige = new MitgliederAnredeAnzeigen();
            } else if (Steuerung.isStatus(Status.MEIN_PASSWORT)) {
                anzeige = new MeinPasswortAnzeigen();
            } else if (Steuerung.isStatus(Status.EMAIL_MODUL)) {
                anzeige = new EmailModulAnzeigen();
            } else if (Steuerung.isStatus(Status.NEUE_EMAIL)) {
                anzeige = new NeueEmailAnzeigen();
            } else if (Steuerung.isStatus(Status.EMAIL_ADRESSBUCH)) {
                anzeige = new EMail_AdressbauchAnzeigen();
            } else if (Steuerung.isStatus(Status.BESTANDSLISTE)) {
                anzeige = new BestandslisteAnzeigen();
            } else if (Steuerung.isStatus(Status.ARTIKEL_EINTRAGEN)) {
                anzeige = new ArtikelEintragenAnzeigen();
            } else if (Steuerung.isStatus(Status.ARTIKEL_ZUWEISEN)) {
                anzeige = new ArtikelZuweisenAnzeigen();
            } else if (Steuerung.isStatus(Status.BESTAND_VERSCHIEBEN)) {
                anzeige = new BestandVerschiebenAnzeigen();
            } else if (Steuerung.isStatus(Status.NEUES_LAGER)) {
                anzeige = new LagerAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.EMAIL_SIGNATUR)) {
                anzeige = new EMailModuleSignaturAnzeigen();
            } else if (Steuerung.isStatus(Status.EMAIL_ANHANG)) {
                anzeige = new EmailAttachmentAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDERAKTE)) {
                anzeige = new MitgliederakteAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRZEUGAKTE)) {
                anzeige = new FahrzeugakteAnzeigen();
            } else if (Steuerung.isStatus(Status.AUSBILDUNGSPLAN)) {
                anzeige = new AusbildungsplanAnzeigen();
            } else if (Steuerung.isStatus(Status.AUSBILDUNGPLAN_LISTE)) {
                anzeige = new AusbildungplanListeAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDERAKTE_KOMMENTAR)) {
                anzeige = new MitgliederakteKomentarAnzeigen();
            } else if (Steuerung.isStatus(Status.MAENGELMELDUNG_BEARBEITEN)) {
                anzeige = new M\u00e4ngelmeldungBearbeitenAnzeigen();
            } else if (Steuerung.isStatus(Status.LEHRGANG_ANLEGEN)) {
                anzeige = new LehrgangAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.LEHRGANG_KONFIGURIEREN)) {
                anzeige = new LehrgangZuordnungAnzeigen();
            } else if (Steuerung.isStatus(Status.LIZENZ_KEY)) {
                anzeige = new ProduktKeyEintragenAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRZEUG_BESCHREIBUNG)) {
                anzeige = new FahrzeugGruppeAnzeigen();
            } else if (Steuerung.isStatus(Status.MAENGELMELDUNG_KOMMENTAR)) {
                anzeige = new M\u00e4ngelmeldungKommentarAnzeigen();
            } else if (Steuerung.isStatus(Status.DIENSTGRAD_ANLEGEN)) {
                anzeige = new DienstgradAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.AUSBILDUNGSINHALTE_TAUSCHEN)) {
                anzeige = new AusbildungsinhalteTauschenAnzeigen();
            } else if (Steuerung.isStatus(Status.ATEMSCHUTZPASS_EINTRAG)) {
                anzeige = new AtemschutzpassEintragAnzeigen();
            } else if (Steuerung.isStatus(Status.ATEMSCHUTZPASS)) {
                anzeige = new AtemschutzpassListeAnzeigen();
            } else if (Steuerung.isStatus(Status.ABRECHNUNG)) {
                anzeige = new AbrechnungAnzeigen();
            } else if (Steuerung.isStatus(Status.ABRECHNUNG_ARTIKEL)) {
                anzeige = new AbrechnungArtikelAnzeigen();
            } else if (Steuerung.isStatus(Status.MANUELLE_VERBUCHUNG)) {
                anzeige = new AbrechnungManuelleVerbuchungAnzeigen();
            } else if (Steuerung.isStatus(Status.ARTIKELKLASSE_ANLEGEN)) {
                anzeige = new AbrechnungArtikelklasseAnzeigen();
            } else if (Steuerung.isStatus(Status.KONTO_ANLEGEN)) {
                anzeige = new AbrechnungKontoAnzeigen();
            } else if (Steuerung.isStatus(Status.GEBURTSTAG_LISTE)) {
                anzeige = new GeburtstagListeAnzeigen();
            } else if (Steuerung.isStatus(Status.DEBUG)) {
                anzeige = new DebugAnzeigen();
            } else if (Steuerung.isStatus(Status.LOGBUCH)) {
                anzeige = new LogbuchAnzeigen();
            } else if (Steuerung.isStatus(Status.SCHICHTPLANER)) {
                anzeige = new SchichtplanerAnzeigen();
            } else if (Steuerung.isStatus(Status.SCHICHT_ANLEGEN)) {
                anzeige = new SchichtAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.SCHICHT_GRUPPE)) {
                anzeige = new SchichtGruppeAnlegenAnzeigen();
            } else if (Steuerung.isStatus(Status.SCHICHTPLAN_LISTE)) {
                anzeige = new SchichtplanListeAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRTENBUCH)) {
                anzeige = new FahrtenbuchAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRTENBUCH_LISTE)) {
                anzeige = new FahrtenbuchListeAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDER_LAUFBAHN)) {
                anzeige = new MitgliedLaufbahnListeAnzeigen();
            } else if (Steuerung.isStatus(Status.LAUFBAHN_EINTRAG)) {
                anzeige = new LaufbahnEintragAnzeigen();
            } else if (Steuerung.isStatus(Status.ADMINBEREICH_STARTEN)) {
                anzeige = new AdministratorAnzeigen();
            } else if (Steuerung.isStatus(Status.URLAUBSPLANER)) {
                anzeige = new UrlaubsplanerAnzeigen();
            } else if (Steuerung.isStatus(Status.URLAUBSPLAN_LISTE)) {
                anzeige = new UrlaubsplanListeAnzeigen();
            } else if (Steuerung.isStatus(Status.STATISTIK)) {
                anzeige = new StatistikAnzeigen();
            } else if (Steuerung.isStatus(Status.BEF\u00d6RDERUNG_KONFIGURIEREN)) {
                anzeige = new Bef\u00f6rderungAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDER_HISTORY)) {
                anzeige = new MitgliederHistoryAnzeigen();
            } else if (Steuerung.isStatus(Status.CLIENTS)) {
                anzeige = new ClientsAnzeigen();
            } else if (Steuerung.isStatus(Status.KATEGORIEN_EDITIEREN)) {
                anzeige = new KategorienEditierenAnzeigen();
            } else if (Steuerung.isStatus(Status.LEHRGANG_LISTE_OPTIONEN)) {
                anzeige = new LehrgangListeOptionenAnzeigen();
            } else if (Steuerung.isStatus(Status.ANWESENHEIT_LISTE_OPTIONEN)) {
                anzeige = new AnwesenheitListeOptionenAnzeigen();
            } else if (Steuerung.isStatus(Status.PROTOKOLL)) {
                anzeige = new ProtokollAnzeigen();
            } else if (Steuerung.isStatus(Status.ANWESENHEITSTABELLE_PRO_MITGLIED)) {
                anzeige = new AnwesenheitsTabelleProMitgliedAnzeigen();
            } else if (Steuerung.isStatus(Status.EHRUNGEN_KONFIGURATION)) {
                anzeige = new EhrungenKonfigurationAnzeigen();
            } else if (Steuerung.isStatus(Status.UNWETTERWARNUNG)) {
                anzeige = new UnwetterwarnungAnzeigen();
            } else if (Steuerung.isStatus(Status.MASSENVERBUCHUNG)) {
                anzeige = new AbrechnungMassenverbuchungAnzeigen();
            } else if (Steuerung.isStatus(Status.MITGLIEDER_VERFUEGBARKEIT)) {
                anzeige = new MitgliederVerfuegbarkeiAnzeigen();
            } else if (Steuerung.isStatus(Status.FAHRZEUG_BELEGUNG)) {
                anzeige = new FahrzeugBelegungAnzeigen();
            } else if (Steuerung.isStatus(Status.VERANSTALTUNG_NOTIFICATON)) {
                anzeige = new EMailNotificationAnzeigen();
            } else if (Steuerung.isStatus(Status.TERMIN_DISPLAY)) {
                anzeige = new TerminDisplayAnzeigen();
            } else {
                JOptionPane.showMessageDialog(null, Konstante.FEHLER_AUFRUFEN_MODUL, "Fehlermeldung", 0);
            }
            ((Anzeige)anzeige).ausfuehren();
        } else {
            logbuchEingabe.NeuerEintag("Programm wird beendet...");
            DatenbankZugriff.disconnectSSHServer();
            logging.logInfo((Object)"Programm wird beendet");
            System.exit(0);
        }
    }

    public static final void setStatus(Status status) {
        Steuerung.status = status;
    }

    public static final Status getStatus() {
        return status;
    }

    public static final boolean isStatus(Status status) {
        return Steuerung.status.equals((Object)status);
    }
}

