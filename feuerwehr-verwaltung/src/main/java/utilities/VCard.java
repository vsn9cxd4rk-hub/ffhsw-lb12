/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities;

import data.tabellen.mitglied.TabelleMitglied;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import utilities.Utils;

public class VCard {
    public static void export(String ordner, String seperator, int gruppenID) {
        TabelleMitglied tabMiglied = new TabelleMitglied();
        File file = new File(String.valueOf(ordner) + "\\contacts.csv");
        if (file.exists()) {
            file.delete();
            logging.logInfo((Object)"vCard existiert bereits...Datei wird gel\u00f6scht");
        }
        try {
            file.createNewFile();
            String[] headline = new String[]{"First Name", "Middle Name", "Last Name", "Title", "Suffix", "Initials", "Web Page", "Gender", "Birthday", "Anniversary", "Location", "Language", "Internet Free Busy", "Notes", "E-mail Address", "E-mail 2 Address", "E-mail 3 Address", "Primary Phone", "Home Phone", "Home Phone 2", "Mobile Phone", "Pager", "Home Fax", "Home Address", "Home Street", "Home Street 2", "Home Street 3", "Home Address PO Box", "Home City", "Home State", "Home Postal Code", "Home Country", "Spouse", "Children", "Manager's Name", "Assistant's Name", "Referred By", "Company Main Phone", "Business Phone", "Business Phone 2", "Business Fax", "Assistant's Phone", "Company", "Job Title", "Department", "Office Location", "Organizational ID Number", "Profession", "Account", "Business Address", "Business Street", "Business Street 2", "Business Street 3", "Business Address PO Box", "Business City", "Business State", "Business Postal Code", "Business Country", "Other Phone", "Other Fax", "Other Address", "Other Street", "Other Street 2", "Other Street 3", "Other Address PO Box", "Other City", "Other State", "Other Postal Code", "Other Country", "Callback", "Car Phone", "ISDN", "Radio Phone", "TTY/TDD Phone", "Telex", "User 1", "User 2", "User 3", "User 4", "Keywords", "Mileage", "Hobby", "Billing Information", "Directory Server", "Sensitivity", "Priority", "Private", "Categories"};
            String[] mitgliederListe = Utils.listToArray(tabMiglied.getVCardExport(gruppenID, seperator));
            FileWriter writer = new FileWriter(file, true);
            int h = 0;
            while (h < headline.length) {
                writer.write(headline[h]);
                writer.write(seperator);
                ++h;
            }
            writer.write(System.getProperty("line.separator"));
            writer.write(System.getProperty("line.separator"));
            int m = 0;
            while (m < mitgliederListe.length) {
                writer.write(mitgliederListe[m]);
                writer.write(System.getProperty("line.separator"));
                ++m;
            }
            writer.flush();
            writer.close();
            logging.logInfo((Object)"Schlie\u00dfe vCard Export Datei");
        }
        catch (IOException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

