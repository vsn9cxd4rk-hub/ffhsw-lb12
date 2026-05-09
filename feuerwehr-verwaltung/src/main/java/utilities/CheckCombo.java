/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import logging.logging;
import utilities.CheckComboRenderer;
import utilities.CheckComboStore;

public class CheckCombo
implements ActionListener {
    public static Boolean[] stateArray;

    public static JComboBox<Object> getComboboxWithCheckBoxes(String[] checkBoxItems, int[] checkBoxItemID, Boolean[] checkBOxBelegung) {
        JComboBox<Object> combo = new CheckCombo().getContent(checkBoxItems, checkBoxItemID, checkBOxBelegung);
        stateArray = new Boolean[checkBoxItems.length];
        stateArray = checkBOxBelegung;
        return combo;
    }

    public JComboBox<Object> getContent(String[] checkBoxItems, int[] checkBoxItemID, Boolean[] checkBOxBelegung) {
        CheckComboStore[] stores = new CheckComboStore[checkBoxItems.length];
        int j = 0;
        while (j < checkBoxItems.length) {
            stores[j] = new CheckComboStore(checkBoxItems[j], checkBoxItemID[j], checkBOxBelegung[j], j);
            ++j;
        }
        JComboBox<Object> combo = new JComboBox<Object>(stores);
        combo.setPreferredSize(new Dimension(150, 25));
        combo.setRenderer(new CheckComboRenderer());
        combo.addActionListener(this);
        return combo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        CheckComboStore store = (CheckComboStore)cb.getSelectedItem();
        CheckComboRenderer ccr = (CheckComboRenderer)cb.getRenderer();
        store.state = store.state == false;
        ccr.checkBox.setSelected(store.state);
        logging.logInfo((Object)("Organisation Ausgew\u00e4hlt: (Name: " + store.name + "), (OrganisationID: " + store.id + "), (Status: " + store.state + "), (Position: " + store.pos + ")"));
        CheckCombo.stateArray[store.pos] = store.state;
    }
}

