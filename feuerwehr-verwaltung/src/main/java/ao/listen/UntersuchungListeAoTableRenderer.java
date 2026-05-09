/*
 * Decompiled with CFR 0.152.
 */
package ao.listen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import run.runApplication;
import utilities.TimeCalculation;

public class UntersuchungListeAoTableRenderer
implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel editor = new JLabel();
        if (value != null) {
            if (value.toString().startsWith("D-->") | value.toString().startsWith("D--!") && value.toString().length() >= 4) {
                if (runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste").equals("0") && value.toString().startsWith("D-->")) {
                    editor.setText(TimeCalculation.parseShortDateForGUI(value.toString().substring(4, value.toString().length())));
                } else {
                    editor.setText(TimeCalculation.parseDateForGUI(value.toString().substring(4, value.toString().length())));
                }
                String tabgeBis = TimeCalculation.getTageBisOhneDebug(value.toString().substring(4, value.toString().length()));
                if (tabgeBis.contains("abgelaufen")) {
                    editor.setForeground(Color.red);
                    editor.setFont(new Font("Arial", 1, 12));
                } else if (tabgeBis.contains("Morgen") | tabgeBis.contains("Heute")) {
                    editor.setForeground(Color.orange);
                    editor.setFont(new Font("Arial", 1, 12));
                } else {
                    editor.setForeground(null);
                    editor.setFont(null);
                }
            } else {
                editor.setText(value.toString());
                editor.setForeground(null);
                editor.setFont(null);
            }
            return editor;
        }
        return editor;
    }
}

