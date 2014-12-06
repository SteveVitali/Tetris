import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class HighScoresView extends JPanel {

    private HighScoresModel model;
    private JTable scoresTable;

    public HighScoresView(HighScoresModel model) {
        this.model = model;
        setBackground(AppController.BG_COLOR);

        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("scores")) {
                    refreshTable();
                }
            }
        });

        scoresTable = new JTable(model.getTableModel());
        scoresTable.setDefaultRenderer(Object.class, new ScoresTableCellRenderer());
        scoresTable.setOpaque(false);
        scoresTable.setRowHeight(32);
        scoresTable.setBorder(BorderFactory.createEmptyBorder());
        scoresTable.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(scoresTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane);
    }
    public void refreshTable() {
        scoresTable.setModel(model.getTableModel());
    }

    class ScoresTableCellRenderer extends JLabel implements TableCellRenderer {

        public ScoresTableCellRenderer() {
            setOpaque(true);
            setForeground(Color.white);
            setBackground(new Color(36,36,36));
            setFont(new Font("Helvetica", Font.PLAIN, 14));
            EmptyBorder borderPad = new EmptyBorder(new Insets(1,4,1,4));
            Border actualBorder = BorderFactory.createLineBorder(new Color(54,54,54),1);
            setBorder(new CompoundBorder(actualBorder, borderPad));
        }
        public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column)
        {
            setText(value.toString());
            return this;
        }
    }
}