package utilities.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import utilities.MyColor;

public class ClientAoTableRenderer implements TableCellRenderer {

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel editor = new JLabel();
      if(isSelected) {
         editor.setBackground(Color.lightGray);
         editor.setOpaque(true);
      }

      if(value != null) {
         if(value.toString().equals("Offline")) {
            editor.setText(value.toString());
            editor.setForeground(Color.red);
            editor.setFont(new Font("Arial", 1, 12));
         } else if(value.toString().equals("Online")) {
            editor.setText(value.toString());
            editor.setForeground(MyColor.FOREST_GREEN);
            editor.setFont(new Font("Arial", 1, 12));
         } else {
            editor.setText(value.toString());
            editor.setForeground((Color)null);
            editor.setFont((Font)null);
         }

         return editor;
      } else {
         return editor;
      }
   }
}
