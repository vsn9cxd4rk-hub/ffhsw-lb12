/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.listen;

import ao.AbstractFenster;
import ao.listen.BeteiligungUebersichtListeAO;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import utilities.Konstante;
import utilities.Utils;

public class AnwesenheitsTabelleProMitgliedAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelMitgliederListe;
    public JTable table;
    private JScrollPane scrollpane;
    private static String nameSelektiertesMitglied;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum");
            this.add("Uhrzeit Beginn");
            this.add("Uhrzeit Ende");
            this.add("Name");
        }
    };

    public AnwesenheitsTabelleProMitgliedAO() {
        super("FeuerwehrManagementSystem - Anwesenheitstabelle");
        logging.logInfo((Object)"Starte: AnwesenheitsTabelleProMitgliedAO");
    }

    protected void buttonErstellen() {
        nameSelektiertesMitglied = BeteiligungUebersichtListeAO.mitglieder.getSelectedItem().toString();
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.modulBeschreibung = new JLabel("Anwesenheitstabelle f\u00fcr " + nameSelektiertesMitglied);
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
    }

    protected void setzeAuswahllisten() {
        this.defaultTableModelMitgliederListe = new DefaultTableModel(10, 9);
        this.defaultTableModelMitgliederListe.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelMitgliederListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 600));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        this.table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                e.getClickCount();
            }
        });
        this.scrollpane = new JScrollPane(this.table);
        this.scrollpane.setVerticalScrollBarPolicy(22);
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(1200, 768);
        this.setTitle("FeuerwehrManagementSystem - Anwesenheitstabelle f\u00fcr " + nameSelektiertesMitglied);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Anwesenheitstabelle f\u00fcr " + nameSelektiertesMitglied;
                Utils.printJTable(headerText, AnwesenheitsTabelleProMitgliedAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitsTabelleProMitgliedAO.this.chooser.setFileSelectionMode(1);
                AnwesenheitsTabelleProMitgliedAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + AnwesenheitsTabelleProMitgliedAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = AnwesenheitsTabelleProMitgliedAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(AnwesenheitsTabelleProMitgliedAO.this.table, new File(outputOrdner), "Anwesenheitsliste_" + nameSelektiertesMitglied);
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleAnwesenheit().getAlleAnwesendeVeranstaltungenProMitglied(new TabelleMitglied().getIdByGuiString(nameSelektiertesMitglied)), this.headname);
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
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

