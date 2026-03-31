package utilities.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyDefaultTableRenderer implements TableCellRenderer {

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel editor = new JLabel();
      if(isSelected) {
         editor.setBackground(Color.lightGray);
         editor.setOpaque(true);
      }

      if(value != null) {
         editor.setText(value.toString());
         editor.setFont(new Font("Arial", 0, 12));
      }

      return editor;
   }
}
