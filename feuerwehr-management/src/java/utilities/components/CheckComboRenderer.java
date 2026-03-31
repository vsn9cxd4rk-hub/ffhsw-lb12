package utilities.components;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import utilities.components.CheckComboStore;

class CheckComboRenderer implements ListCellRenderer {

   JCheckBox checkBox = new JCheckBox();


   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      CheckComboStore store = (CheckComboStore)value;
      this.checkBox.setText(store.name);
      this.checkBox.setSelected(store.state.booleanValue());
      return this.checkBox;
   }
}
