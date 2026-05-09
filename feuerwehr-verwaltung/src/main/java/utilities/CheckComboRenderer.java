/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import utilities.CheckComboStore;

class CheckComboRenderer
implements ListCellRenderer<Object> {
    JCheckBox checkBox = new JCheckBox();

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        CheckComboStore store = (CheckComboStore)value;
        this.checkBox.setText(store.name);
        this.checkBox.setSelected(store.state);
        return this.checkBox;
    }
}

