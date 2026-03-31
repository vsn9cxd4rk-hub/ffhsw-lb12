package utilities.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import logging.logging;
import utilities.components.CheckComboRenderer;
import utilities.components.CheckComboStore;

public class CheckCombo implements ActionListener {

   public static Boolean[] stateArray;


   public static JComboBox getComboboxWithCheckBoxes(String[] checkBoxItems, int[] checkBoxItemID, Boolean[] checkBOxBelegung) {
      JComboBox combo = (new CheckCombo()).getContent(checkBoxItems, checkBoxItemID, checkBOxBelegung);
      stateArray = new Boolean[checkBoxItems.length];
      stateArray = checkBOxBelegung;
      return combo;
   }

   public JComboBox getContent(String[] checkBoxItems, int[] checkBoxItemID, Boolean[] checkBOxBelegung) {
      CheckComboStore[] stores = new CheckComboStore[checkBoxItems.length];

      for(int combo = 0; combo < checkBoxItems.length; ++combo) {
         stores[combo] = new CheckComboStore(checkBoxItems[combo], checkBoxItemID[combo], checkBOxBelegung[combo], combo);
      }

      JComboBox var6 = new JComboBox(stores);
      var6.setPreferredSize(new Dimension(150, 25));
      var6.setRenderer(new CheckComboRenderer());
      var6.addActionListener(this);
      return var6;
   }

   public void actionPerformed(ActionEvent e) {
      JComboBox cb = (JComboBox)e.getSource();
      CheckComboStore store = (CheckComboStore)cb.getSelectedItem();
      CheckComboRenderer ccr = (CheckComboRenderer)cb.getRenderer();
      ccr.checkBox.setSelected((store.state = Boolean.valueOf(!store.state.booleanValue())).booleanValue());
      logging.logInfo("Organisation Ausgewählt: (Name: " + store.name + "), (OrganisationID: " + store.id + "), (Status: " + store.state + "), (Position: " + store.pos + ")");
      stateArray[store.pos] = store.state;
   }
}
