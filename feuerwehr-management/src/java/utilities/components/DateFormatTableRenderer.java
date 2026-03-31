package utilities.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import run.runApplication;
import utilities.MyColor;
import utilities.TimeCalculation;

public class DateFormatTableRenderer implements TableCellRenderer {

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel editor = new JLabel();
      if(value == null) {
         return editor;
      } else {
         String[] data = value.toString().split(";");
         String value1 = data[0];
         if(value1.toString().startsWith("D-->") | value1.toString().startsWith("D--!") && value1.toString().length() >= 4) {
            if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("0") && value1.toString().startsWith("D-->")) {
               editor.setText(TimeCalculation.parseShortDateForGUI(value1.toString().substring(4, value1.toString().length())));
            } else {
               editor.setText(TimeCalculation.parseDateForGUI(value1.toString().substring(4, value1.toString().length())));
            }

            String tabgeBis = TimeCalculation.getTageBisOhneDebug(value1.toString().substring(4, value1.toString().length()));
            if(tabgeBis.contains("abgelaufen")) {
               editor.setBackground(Color.red);
               editor.setForeground(Color.white);
               editor.setFont(new Font("Arial", 1, 12));
               editor.setOpaque(true);
            } else if(tabgeBis.contains("Morgen") | tabgeBis.contains("Heute")) {
               editor.setForeground(Color.black);
               editor.setBackground(Color.orange);
               editor.setFont(new Font("Arial", 1, 12));
               editor.setOpaque(true);
            } else if(tabgeBis.contains("Tage")) {
               int tage = Integer.parseInt(data[1]) * 30;
               if(Integer.parseInt(tabgeBis.substring(0, tabgeBis.indexOf(" Tage"))) >= tage) {
                  editor.setForeground(Color.white);
                  editor.setBackground(MyColor.FOREST_GREEN);
                  editor.setFont(new Font("Arial", 1, 12));
                  editor.setOpaque(true);
               } else {
                  editor.setForeground(Color.black);
                  editor.setBackground(Color.orange);
                  editor.setFont(new Font("Arial", 1, 12));
                  editor.setOpaque(true);
               }
            } else {
               editor.setForeground((Color)null);
               editor.setFont((Font)null);
            }
         } else {
            editor.setText(value1.toString());
            editor.setForeground((Color)null);
            editor.setFont((Font)null);
         }

         return editor;
      }
   }
}
