/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.listen;

import ao.AbstractFenster;
import ao.utils.StartBildschirmAO;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Vector;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class AnwesenheitListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonOptionen;
    public static JButton buttonStandard;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelLehrgang;
    public static JTable table;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private static Vector<String> headname;

    static {
        headname = new Vector<String>(){
            private static final long serialVersionUID = 1L;
            {
                if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
                    this.add("Dienstgrad");
                }
                this.add("Name");
                this.add("Vorname");
                this.add("Unterschrift");
            }
        };
    }

    public AnwesenheitListeAO() {
        super("FeuerwehrManagementSystem - Anwesenheit Liste");
        logging.logInfo((Object)"Starte: AnwesenheitListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonOptionen = new JButton("Filter / Optionen");
        buttonStandard = new JButton("Standard Wiederherstellen");
        this.modulBeschreibung = new JLabel("Anwesenheit Liste ");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(1200, 768);
        this.setTitle("FeuerwehrManagementSystem - Lehrgang Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelLehrgang = new DefaultTableModel(5, 4);
        this.defaultTableModelLehrgang.setColumnIdentifiers(headname);
        table = new JTable(this.defaultTableModelLehrgang);
        table.setPreferredScrollableViewportSize(new Dimension(1100, 570));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        try {
            int mitgliederZahlen = new TabelleMitglied().getMitgliederCountGruppe1();
            AnwesenheitListeAO.dynacmicRowHigh(mitgliederZahlen);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(buttonStandard);
        this.add(this.buttonOptionen);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonZurueck);
        buttonStandard.setVisible(false);
    }

    public static void dynacmicRowHigh(int mitgliederZahlen) {
        int schriftGr\u00f6\u00dfe = Integer.parseInt(runApplication.EINSTELLUNGEN.get("schfiftgr\u00f6\u00dfeAnwesenheitsliste"));
        table.setFont(new Font("Arial", 1, schriftGr\u00f6\u00dfe));
        if (schriftGr\u00f6\u00dfe != 26) {
            logging.logInfo((Object)("Benutzerdefinierte Schriftgr\u00f6\u00dfe f\u00fcr die Anwesenheitsliste (" + schriftGr\u00f6\u00dfe + ")"));
            table.setRowHeight(schriftGr\u00f6\u00dfe + 4);
        } else {
            logging.logInfo((Object)"Nutze Standard SchriftGr\u00f6\u00dfe! - Automatische ZeilenAnpassung ist aktiv!");
            if (mitgliederZahlen <= 36) {
                table.setRowHeight(40);
                logging.logInfo((Object)("Initialisiere Anwesenheitsliste mit Zeilenh\u00f6he: 40, Mitgliederzahlen: " + mitgliederZahlen));
            } else if (mitgliederZahlen >= 36 && mitgliederZahlen <= 40) {
                table.setRowHeight(36);
                logging.logInfo((Object)("Initialisiere Anwesenheitsliste mit Zeilenh\u00f6he: 36, Mitgliederzahlen: " + mitgliederZahlen));
            } else if (mitgliederZahlen >= 40 && mitgliederZahlen <= 72) {
                table.setRowHeight(40);
                logging.logInfo((Object)("Initialisiere Anwesenheitsliste mit Zeilenh\u00f6he: 40, Mitgliederzahlen: " + mitgliederZahlen));
            } else if (mitgliederZahlen >= 72 && mitgliederZahlen <= 76) {
                table.setRowHeight(36);
                logging.logInfo((Object)("Initialisiere Anwesenheitsliste mit Zeilenh\u00f6he: 36, Mitgliederzahlen: " + mitgliederZahlen));
            } else {
                table.setRowHeight(40);
                logging.logInfo((Object)("Initialisiere Anwesenheitsliste mit Zeilenh\u00f6he: 40, Mitgliederzahlen: " + mitgliederZahlen));
            }
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        buttonStandard.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    int mitgliederZahlen = new TabelleMitglied().getMitgliederCountGruppe1();
                    AnwesenheitListeAO.dynacmicRowHigh(mitgliederZahlen);
                    ((DefaultTableModel)table.getModel()).setDataVector(new TabelleAnwesenheit().getAllDataForList(), headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                buttonStandard.setVisible(false);
            }
        });
        this.buttonOptionen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ANWESENHEIT_LISTE_OPTIONEN);
                Steuerung.steuerung();
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Anwesenheitsliste";
                if (runApplication.EINSTELLUNGEN.get("druckAnwesenheitsListeMode").equals("1")) {
                    String msg;
                    headerText = msg = JOptionPane.showInputDialog(null, "Bitte geben Sie die Tabelle\u00fcberschrift an:                            \n\n", "Eingabeaufforderung", 3, null, null, "Anwesenheitsliste").toString();
                } else if (runApplication.EINSTELLUNGEN.get("druckAnwesenheitsListeMode").equals("2")) {
                    try {
                        String msg;
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        JFrame frame = new JFrame("Frage");
                        Object[] veranstaltungListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltung());
                        String veranstaltungsAuswahl = (String)JOptionPane.showInputDialog(frame, "Bitte w\u00e4hlen Sie die Veranstaltung aus f\u00fcr welche die Anwesnheitsliste\ngedruckt werden soll.\n\nHINWEIS:\nWenn nur eine Liste gedruckt werden soll ohne Veranstaltungsinformation\nw\u00e4hlen Sie bitte \"keinen\" Eintrag aus!\n\n", "Frage", 3, null, veranstaltungListe, null);
                        headerText = veranstaltungsAuswahl != null ? "Anwesenheitsliste - " + veranstaltungsAuswahl : (msg = JOptionPane.showInputDialog(null, "Bitte geben Sie die Tabelle\u00fcberschrift an:                            \n\n", "Eingabeaufforderung", 3, null, null, "Anwesenheitsliste").toString());
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
                Utils.printJTable(headerText, table, OrientationRequested.PORTRAIT, false, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitListeAO.this.chooser.setFileSelectionMode(1);
                AnwesenheitListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + AnwesenheitListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = AnwesenheitListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(table, new File(outputOrdner), "AnwesenheitListe");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)table.getModel()).setDataVector(new TabelleAnwesenheit().getAllDataForList(), headname);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        if (MyEvent.event.equals("0x0360")) {
            StartBildschirmAO.startDialogText.setText("Anwesenheitsliste wird gedruckt ...");
            this.setVisible(false);
            String headerText = runApplication.EINSTELLUNGEN.get("Anwesenheitsliste_DirektDruck_HeaderText");
            if (runApplication.EINSTELLUNGEN.get("Anwesenheitsliste_DirektDruck_HeaderText_MitDatum").equals("1")) {
                headerText = String.valueOf(runApplication.EINSTELLUNGEN.get("Anwesenheitsliste_DirektDruck_HeaderText")) + " " + SbcUtils.timeStamp((String)"dd.MM.yyyy");
            }
            Utils.printJTable(headerText, table, OrientationRequested.PORTRAIT, false, false);
            logging.logInfo((Object)"Druckauftrage wurde erfolgrich erstellt und Programm wird beendet....");
            System.exit(0);
        }
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

