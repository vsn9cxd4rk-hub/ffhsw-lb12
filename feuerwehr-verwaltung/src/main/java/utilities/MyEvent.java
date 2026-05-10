/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import javax.swing.JOptionPane;
import logging.logging;

public class MyEvent {
    public static String event;

    public static void setEvent(String eventcode) {
        event = eventcode;
        logging.logBasicClassesInfo("Eventcode wurde gesetzt: " + eventcode);
    }

    public static void initEvent() {
        event = "0";
    }

    public static void EventError(String eventcode) {
        JOptionPane.showMessageDialog(null, "Event Error mit Event: " + eventcode, "Fehler", 0);
        logging.logBasicClassesInfo("Event Error mit Event : " + eventcode);
    }
}

