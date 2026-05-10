/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import logging.logging;

public class MyProperties {
    private File myfile;
    private Properties prop;

    public MyProperties(String pathname) {
        this.myfile = new File(pathname);
        this.prop = new Properties();
    }

    public boolean sourceFileExists() {
        return this.myfile.exists();
    }

    public boolean sourceFileDelete() {
        return this.myfile.delete();
    }

    public void putVar(String key, Object value) {
        this.prop.put(key, value);
    }

    public Object getVar(String key) {
        return this.prop.get(key);
    }

    public void loadVars() {
        try {
            FileInputStream fis = new FileInputStream(this.myfile);
            this.prop.load(fis);
            fis.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Beim Laden ist ein Fehler aufgetreten...", "Fehler", 0);
        }
    }

    public void saveVars() {
        try {
            FileOutputStream fos = new FileOutputStream(this.myfile);
            this.prop.store(fos, " ServiceBasicClasses Einstellungen\n Die Datei wurden automatisiert vom Programm erzeugt \n \n");
            fos.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Beim Speichern ist ein Fehler aufgetreten!", "Fehler", 0);
        }
    }

    public void checkPropertiesEntry(String key, Object defaultValue) {
        try {
            FileReader fis = new FileReader(this.myfile);
            BufferedReader br = new BufferedReader(fis);
            String line = null;
            int lineNotFound = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\n");
                if (!parts[0].startsWith(key)) continue;
                ++lineNotFound;
            }
            br.close();
            fis.close();
            if (lineNotFound == 0) {
                this.loadVars();
                this.putVar(key, defaultValue);
                this.saveVars();
                logging.logBasicClassesInfo("Fehlender Eintarg im Properties " + this.myfile + " wurde erzeugt...(" + key + "=" + defaultValue + ")");
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

