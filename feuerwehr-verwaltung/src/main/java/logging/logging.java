/*
 * Decompiled with CFR 0.152.
 */
package logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import utilities.MyProperties;
import utilities.SbcUtils;

public class logging {
    static String logPath;
    static String propertiesPath;
    static String programmName;
    static String programmVersion;
    static String settingsWriteToFile;
    static String settingSize;

    public static void logInit(String LOG_PATH, String PROPERTIES_PATH, String NAME_REMOTE_APPLIKATION, String VERSION_REMOTE_APPLIKATION) {
        logging.logBasicClassesInfo("Starte: ServiceBasicClasses Version: 1.29");
        logging.logBasicClassesInfo("LogInit start...");
        logPath = LOG_PATH;
        programmName = NAME_REMOTE_APPLIKATION;
        propertiesPath = PROPERTIES_PATH;
        programmVersion = VERSION_REMOTE_APPLIKATION;
        MyProperties loggingProperties = new MyProperties(String.valueOf(propertiesPath) + "/logging.properties");
        if (loggingProperties.sourceFileExists()) {
            logging.logBasicClassesInfo("logging.properties == existiert");
            loggingProperties.checkPropertiesEntry("writeFile", "true");
            loggingProperties.checkPropertiesEntry("Size", "10485760");
            MyProperties logeinstellungen = new MyProperties(String.valueOf(propertiesPath) + "/logging.properties");
            logeinstellungen.loadVars();
            logging.logBasicClassesInfo("Load data to varable...");
            settingsWriteToFile = (String)logeinstellungen.getVar("writeFile");
            settingSize = (String)logeinstellungen.getVar("Size");
        } else {
            logging.logBasicClassesInfo("None properties available...load default...");
            settingSize = "10485760";
            settingsWriteToFile = "false";
        }
        logging.logBasicClassesInfo("Log-Path: /" + logPath);
        logging.logBasicClassesInfo("Properties-Path: /" + propertiesPath);
        logging.logBasicClassesInfo("Application: " + programmName);
        logging.logBasicClassesInfo(programmVersion);
        logging.logBasicClassesInfo("LogInit done...");
    }

    public static void logFileWriter(String message) {
        block7: {
            try {
                if (!settingsWriteToFile.equals("true")) break block7;
                if (!new File(logPath).exists()) {
                    new File(logPath).mkdir();
                    logging.logBasicClassesInfo("create Log Folder /" + logPath);
                }
                try {
                    File file = new File(String.valueOf(logPath) + "/" + programmName + "_log.log");
                    File file2 = new File(String.valueOf(logPath) + "/" + programmName + "_log_old.log");
                    if (file.length() > (long)Integer.parseInt(settingSize)) {
                        if (file2.exists()) {
                            file2.delete();
                        }
                        file.renameTo(file2);
                        file.delete();
                        file.createNewFile();
                        logging.logBasicClassesInfo("logdatei voll und wird gel\u00f6scht");
                        FileWriter writer = new FileWriter(file, true);
                        writer.write(message);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                        writer.close();
                        break block7;
                    }
                    FileWriter writer = new FileWriter(file, true);
                    writer.write(message);
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (NullPointerException e1) {
                logging.logBasicClassesError("NullPointerException by creating of LogFile. (Missing: logInit) <-- Initizalisationof LogOutput");
                logging.logBasicClassesError("System.exit(0) will be executed...");
                System.exit(0);
            }
        }
    }

    public static void logInfo(Object message) {
        String output = String.valueOf(SbcUtils.timeStamp("dd.MM.yyyy HH:mm:ss")) + "\t" + programmName + " " + programmVersion + "\tINFO\t" + message;
        System.out.println(output);
        logging.logFileWriter(output);
    }

    public static void logError(Object message) {
        String output = String.valueOf(SbcUtils.timeStamp("dd.MM.yyyy HH:mm:ss")) + "\t" + programmName + " " + programmVersion + "\tERROR\t" + message;
        System.err.println(output);
        logging.logFileWriter(output);
    }

    public static void logPrintStackTrace(Exception message) {
        try {
            StringWriter errors = new StringWriter();
            message.printStackTrace();
            message.printStackTrace(new PrintStream(String.valueOf(logPath) + "/" + programmName + "_LastExcptionOverView_log.log"));
            message.printStackTrace(new PrintWriter(errors));
            String output = errors.toString();
            logging.logFileWriter(output);
        }
        catch (FileNotFoundException e) {
            logging.logBasicClassesError("Error while PrintingStackTrace (Missing LogFile): " + e);
        }
    }

    public static void logWarning(Object message) {
        String output = String.valueOf(SbcUtils.timeStamp("dd.MM.yyyy HH:mm:ss")) + "\t" + programmName + " " + programmVersion + "\tWARNING\t" + message;
        System.out.println(output);
        logging.logFileWriter(output);
    }

    public static void logSQL(Object message) {
        String output = String.valueOf(SbcUtils.timeStamp("dd.MM.yyyy HH:mm:ss")) + "\t" + programmName + " " + programmVersion + "\tSQL\t" + message;
        System.out.println(output);
        logging.logFileWriter(output);
    }

    public static void logBasicClassesInfo(Object message) {
        String output = String.valueOf(SbcUtils.timeStamp("dd.MM.yyyy HH:mm:ss")) + "\t" + "SBC" + " " + "Version: 1.29" + "\tINFO\t" + message;
        System.out.println(output);
    }

    public static void logBasicClassesError(Object message) {
        String output = String.valueOf(SbcUtils.timeStamp("dd.MM.yyyy HH:mm:ss")) + "\t" + "SBC" + " " + "Version: 1.29" + "\tERROR\t" + message;
        System.out.println(output);
    }
}

