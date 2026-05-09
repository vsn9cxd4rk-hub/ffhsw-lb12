/*
 * Decompiled with CFR 0.152.
 */
package ao.administrator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ClientAoTableRenderer
implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel editor = new JLabel();
        if (value != null) {
            if (value.toString().equals("Offline")) {
                editor.setText(value.toString());
                editor.setForeground(Color.red);
                editor.setFont(new Font("Arial", 1, 12));
            } else if (value.toString().equals("Online")) {
                editor.setText(value.toString());
                editor.setForeground(Color.GREEN);
                editor.setFont(new Font("Arial", 1, 12));
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

