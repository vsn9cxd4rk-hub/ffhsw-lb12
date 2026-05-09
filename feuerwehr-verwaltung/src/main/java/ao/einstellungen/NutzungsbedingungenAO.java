/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.einstellungen;

import ao.AbstractFenster;
import ao.utils.StartBildschirmAO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.SbcUtils;

public class NutzungsbedingungenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JTextArea textfield;
    private JScrollPane pane;
    private JCheckBox akzepieren;
    private JButton buttonWeiter;
    private JButton buttonBeenden;

    public NutzungsbedingungenAO() {
        super("Nutzungsbedingungen Version: 3.21");
        logging.logInfo((Object)"Starte: NutzungsbedingungenAO");
    }

    protected void buttonErstellen() {
        this.buttonWeiter = new JButton("Weiter");
        this.buttonBeenden = new JButton("Programm beenden");
        this.akzepieren = new JCheckBox("Nutzungsbedingungen Akzeptieren");
        this.textfield = new JTextArea(20, 40);
        this.textfield.setLineWrap(true);
        this.textfield.setWrapStyleWord(true);
        this.textfield.setEditable(false);
        this.pane = new JScrollPane(this.textfield);
        this.pane.setVerticalScrollBarPolicy(22);
        this.textfield.setText("FeuerwehrManagementSystem\nVersion: 3.21\n\nEntwickelt von: Mark Hermanns, D-41069 M\u00f6nchengladbach\nUnterst\u00fcrzt von: Martin B\u00f6rner, D-04178 Leipzig\n\nhttp://feuerwehrmanagementsystem.de\ninfo@feuerwehrmanagementsystem.de\n\u00a9" + SbcUtils.timeStamp((String)"yyyy") + " Alle Rechte Vorbehalten" + "\n\n" + "------------------------------------------------------------" + "\n" + "Das " + "FeuerwehrManagementSystem" + " wurde speziell f\u00fcr Organisation, Dokumentation und Planung f\u00fcr Freiwillige Feuerwehren entwickelt." + "\n\n" + "Das Programm wurde in der Hoffnung entwickelt, dass es n\u00fctzlich ist. Es wird aber dennoch keine Garantie bzw. Haftung \u00fcbernommen. " + "\n\n" + "Wir w\u00fcrden uns freuen wenn Sie uns helfen unsere Software zu verbessern. Hierf\u00fcr k\u00f6nnen sie das Fehlerformular auf der Download-Seite (http://feuerwehrmanagementsystem.de) nutzen oder uns via E-Mail (info@feuerwehrmanagementsystem.de) kontaktieren." + "\n\n" + "HINWEIS / WICHTIG: Damit die Funktion der Berichtigung geben ist mussten verschiedene Programmteile mit Passwort versehen werden. Die Berechtigungsgruppen sind sebstverst\u00e4dlich editierbar. Unter der Gruppe \"Public\" ist die Konfiguration, wie das Programm gestartet wird. Standard Administrator ist \"admin\", das Passwort wird \u00fcber die Grundkonfiguration angelegt." + "\n\n" + "------------------------------------------------------------" + "\n\n" + "+++ " + "FeuerwehrManagementSystem" + " (Java) +++" + "\n" + "Windows XP, Vista, 7, 8, 8.1, 10" + "\n" + "Java-Laufzeitumgebenung Version 1.7.x oder h\u00f6her" + "\n" + "MySQL5 Datenbank" + "\n" + "Microsoft Word oder Programme zum \u00d6ffnen von DOC-Dateien, PDF Viewer" + "\n" + "Externe Implementierungen: Java ICEPDF, Java JFreeChart, Java iText, Apache Commons NET, MySQL5 DB" + "\n" + "Unterst\u00fctzte Bildschirmaufl\u00f6sungen: 1920 x 1080, 1600x900, 1440x900, 1366x768, 1280 x 1024" + "\n" + "Optimale Aufl\u00f6sungen sind: 1920 x 1080, 1600x900, 1440x900, 1280 x 1024" + "\n\n" + "------------------------------------------------------------" + "\n" + "Programmfunktionen / Kurzbeschreibung:" + "\n" + "- Mitgliederverwaltung inkl. Mitgliederuntersuchungen (G25, G26/3, Atemschutztraining" + "\n" + "- verschiedene Mitgliedergruppen" + "\n" + "- Fahrzeugverwaltung inkl. Vorwarnung f\u00fcr T\u00dcV, SP LKW, Sonstige Wartung" + "\n" + "- Einsatzliste inkl. Statistiken" + "\n" + "- Einsatzbericht erstellen (vorgefertigtes oder eigenes Layout)" + "\n" + "- Brandsicherheitswachen Liste inkl. Statistiken" + "\n" + "- Beteiligungsstatistik" + "\n" + "- Lehrgangsliste" + "\n" + "- Erstellen von M\u00e4ngelmeldungen" + "\n" + "- Erstellen von Verdienstausfallbescheinigung (vorgefertigtes oder eigenes Layout)" + "\n" + "- Erstellen von \u00dcbersichtsberichten" + "\n" + "- Erstellen von Briefen bzw. Rundschreiben an Mitglieder mit eigenem Briefkopf" + "\n" + "- Verwaltung von Dokumenten die vom Programm erstellt wurden (Dateiverwaltung)" + "\n" + "- Ger\u00e4tepr\u00fcfung inkl. Vorwarnung f\u00fcr die Wartung der Ger\u00e4te" + "\n" + "- Lehrgangsverwaltung" + "\n" + "- Generieren von Vorschl\u00e4gen f\u00fcr Lehrgangsmeldungen" + "\n" + "- Lager- / Bestandsverwaltung" + "\n" + "- Artikelverwaltung" + "\n" + "- Ausbildungsplan erstellen und Ausbildungsplanliste" + "\n" + "- Ausbildungsstatistik" + "\n" + "- E-Mail Modul zum Senden von E-Mails" + "\n" + "- Automatisches generieren von E-Mails (konfigurierbar)" + "\n" + "- Benutzer und Berechtigungsverwaltung f\u00fcr den Zugriff mit verschiedenen Profilen" + "\n" + "- Datenbanksicherung / Backup");
    }

    protected void setzeAuswahllisten() {
        this.akzepieren.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (NutzungsbedingungenAO.this.akzepieren.isSelected()) {
                    NutzungsbedingungenAO.this.buttonWeiter.setEnabled(true);
                } else {
                    NutzungsbedingungenAO.this.buttonWeiter.setEnabled(false);
                }
            }
        });
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("Nutzungsbedingungen Version: 3.21");
        this.setSize(500, 410);
        this.setDefaultCloseOperation(3);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.pane);
        this.add(this.akzepieren);
        this.add(this.buttonWeiter);
        this.add(this.buttonBeenden);
        this.buttonWeiter.setEnabled(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonWeiter.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                NutzungsbedingungenAO.this.dispose();
                if (runApplication.JavaWebStart == 1) {
                    Steuerung.setStatus(Status.GRUNDKONFIGURATION_JWS);
                    Steuerung.steuerung();
                } else {
                    Steuerung.setStatus(Status.GRUNDKONFIGURATION);
                    Steuerung.steuerung();
                }
            }
        });
        this.buttonBeenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logging.logInfo((Object)"Nutzungsbedingungen wurden nicht akzeptiert und Programm wird beendet");
                System.exit(0);
            }
        });
    }

    public void fensterAnzeigen() {
        StartBildschirmAO.startDialog.setVisible(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

