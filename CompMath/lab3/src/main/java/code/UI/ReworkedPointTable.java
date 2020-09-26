package code.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ReworkedPointTable  extends JTable {
    private DefaultTableModel model;

    @Override
    public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);

        try {
            tip = getValueAt(rowIndex, colIndex).toString();
        } catch (RuntimeException ignored) {
        }

        return tip;
    }

    public ReworkedPointTable(Object[][] data, Object[] columnNames){
        super(data,columnNames);
        model = new DefaultTableModel(data,columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return double.class;
            }
        };
        this.setDefaultRenderer(int.class,new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
        this.setModel(model);
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
        rowSorter.setRowFilter(null);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void addRow(Object[] row) {
        model.addRow(row);
        model.fireTableRowsInserted(model.getRowCount() -1, model.getRowCount()-1);
    }

    public void removeRow(int number) {
        model.removeRow(number);
        model.fireTableRowsDeleted(number, number);
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

}
