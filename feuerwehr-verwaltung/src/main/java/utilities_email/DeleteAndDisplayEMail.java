/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.Datei
 */
package utilities_email;

import ao.email.EMailModulAO;
import data.tabellen.email.TabelleEMail_ausgang;
import data.tabellen.email.TabelleEMail_empfangen;
import data.tabellen.email.TabelleEMail_entwurf;
import data.tabellen.email.TabelleEMail_gesendet;
import java.io.File;
import logging.logging;
import run.runApplication;
import utilities.Datei;
import utilities.Utils;

public class DeleteAndDisplayEMail {
    public void display() {
        try {
            TabelleEMail_gesendet tabGes = new TabelleEMail_gesendet();
            TabelleEMail_entwurf tabEnt = new TabelleEMail_entwurf();
            StringBuilder build = new StringBuilder();
            build.setLength(0);
            switch (EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()) {
                case "Posteingang Ungelesen": {
                    this.formatMailContend();
                    this.getAttachmentFromServer("empfangende", DeleteAndDisplayEMail.createNachrichtenID());
                    break;
                }
                case "Posteingang Gelesen": {
                    this.formatMailContend();
                    this.getAttachmentFromServer("empfangende", DeleteAndDisplayEMail.createNachrichtenID());
                    break;
                }
                case "Gesendete Objekte": {
                    build.append(tabGes.getBetreff(DeleteAndDisplayEMail.createNachrichtenID()));
                    build.append("\n");
                    build.append("---------------------------------------------------------------------------------------------------------------------------------------------");
                    build.append("\n");
                    build.append("An: " + tabGes.getSender(DeleteAndDisplayEMail.createNachrichtenID()));
                    build.append("\n");
                    build.append("CC: " + tabGes.getCC(DeleteAndDisplayEMail.createNachrichtenID()));
                    build.append("\n");
                    build.append("BCC: " + tabGes.getBCC(DeleteAndDisplayEMail.createNachrichtenID()));
                    build.append("\n");
                    build.append("---------------------------------------------------------------------------------------------------------------------------------------------");
                    build.append("\n\n");
                    build.append(tabGes.getNachricht(DeleteAndDisplayEMail.createNachrichtenID()));
                    EMailModulAO.textfeld_email.setText(build.toString());
                    this.getAttachmentFromServer("gesendet", DeleteAndDisplayEMail.createNachrichtenID());
                    break;
                }
                case "Entwurf": {
                    EMailModulAO.textfeld_email.setText(tabEnt.getNachricht(DeleteAndDisplayEMail.createNachrichtenID()));
                    this.getAttachmentFromServer("entwurf", DeleteAndDisplayEMail.createNachrichtenID());
                }
            }
            EMailModulAO.textfeld_email.setCaretPosition(0);
        }
        catch (Exception e) {
            logging.logError((Object)e);
        }
    }

    void formatMailContend() throws Exception {
        TabelleEMail_empfangen tabEmpf = new TabelleEMail_empfangen();
        String mailText = tabEmpf.getNachricht(DeleteAndDisplayEMail.createNachrichtenID());
        logging.logInfo((Object)mailText);
        if (mailText.equals("WeHaveAnHTMLFileForClient")) {
            logging.logInfo((Object)"Receive File from Server --> Client");
            EMailModulAO.textfeld_email.setPage(new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/temp/email" + DeleteAndDisplayEMail.createNachrichtenID() + ".html").getAbsoluteFile().toURI().toURL());
            tabEmpf.updateReadStatus(DeleteAndDisplayEMail.createNachrichtenID());
        } else {
            EMailModulAO.textfeld_email.setText(mailText);
            tabEmpf.updateReadStatus(DeleteAndDisplayEMail.createNachrichtenID());
        }
    }

    private void getAttachmentFromServer(String ordner, int id) throws Exception {
        if (this.AttachmentAvailable(ordner, id)) {
            EMailModulAO.buttonAttachment.setVisible(true);
        } else {
            EMailModulAO.buttonAttachment.setVisible(false);
        }
    }

    public boolean AttachmentAvailable(String ordner, int id) throws Exception {
        File attOrdner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/" + ordner + "/" + id);
        return attOrdner.exists();
    }

    public static int createNachrichtenID() {
        boolean error = false;
        int ergebnis = 0;
        do {
            String selectedListItem = (String)EMailModulAO.mailListe.getSelectedValue();
            error = false;
            try {
                ergebnis = Integer.parseInt(selectedListItem.substring(11, selectedListItem.length()));
                error = false;
            }
            catch (NumberFormatException e) {
                int aktuellePosition = EMailModulAO.mailListe.getSelectedIndex();
                EMailModulAO.mailListe.setSelectedIndex(aktuellePosition - 1);
                error = true;
            }
        } while (error);
        return ergebnis;
    }

    public void delete() {
        try {
            switch (EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()) {
                case "Posteingang Ungelesen": {
                    File folderE_ungelesen = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Empfangende/" + DeleteAndDisplayEMail.createNachrichtenID());
                    if (folderE_ungelesen.exists()) {
                        Datei.delFolder((File)folderE_ungelesen);
                    }
                    new TabelleEMail_empfangen().deleteNachricht(DeleteAndDisplayEMail.createNachrichtenID());
                    EMailModulAO.mailListe.setListData((Object[])Utils.listToArray(new TabelleEMail_empfangen().getAllMails(0)));
                    break;
                }
                case "Posteingang Gelesen": {
                    File folderE_gelesen = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Empfangende/" + DeleteAndDisplayEMail.createNachrichtenID());
                    if (folderE_gelesen.exists()) {
                        Datei.delFolder((File)folderE_gelesen);
                    }
                    new TabelleEMail_empfangen().deleteNachricht(DeleteAndDisplayEMail.createNachrichtenID());
                    EMailModulAO.mailListe.setListData((Object[])Utils.listToArray(new TabelleEMail_empfangen().getAllMails(1)));
                    break;
                }
                case "Gesendete Objekte": {
                    File folderG = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Gesendet/" + DeleteAndDisplayEMail.createNachrichtenID());
                    if (folderG.exists()) {
                        Datei.delFolder((File)folderG);
                    }
                    new TabelleEMail_gesendet().deleteNachricht(DeleteAndDisplayEMail.createNachrichtenID());
                    EMailModulAO.mailListe.setListData((Object[])Utils.listToArray(new TabelleEMail_gesendet().getAllSendMails()));
                    break;
                }
                case "Entwurf": {
                    File folderE = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Entwurf/" + DeleteAndDisplayEMail.createNachrichtenID());
                    if (folderE.exists()) {
                        Datei.delFolder((File)folderE);
                    }
                    new TabelleEMail_entwurf().deleteNachricht(DeleteAndDisplayEMail.createNachrichtenID());
                    EMailModulAO.mailListe.setListData((Object[])Utils.listToArray(new TabelleEMail_entwurf().getAllEntwurfMails()));
                    break;
                }
                case "Postausgang": {
                    new TabelleEMail_ausgang().deleteNachricht(DeleteAndDisplayEMail.createNachrichtenID());
                    EMailModulAO.mailListe.setListData((Object[])Utils.listToArray(new TabelleEMail_ausgang().getAllSendMails()));
                }
            }
            EMailModulAO.mailListe.setSelectedIndex(0);
        }
        catch (Exception e1) {
            logging.logError((Object)e1);
        }
    }
}

