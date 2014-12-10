
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
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

    private AppController app;
    private HighScoresModel model;
    private JTable scoresTable;

    public HighScoresView(AppController a, HighScoresModel m) {
        this.app = a;
        this.model = m;

        // Listen for changes to model scores to update table appropriately
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
        scoresTable.setRowHeight(32);
        scoresTable.setBorder(BorderFactory.createEmptyBorder());
        scoresTable.setOpaque(false);
        scoresTable.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(scoresTable);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);
    }
    public void refreshTable() {
        // Updating the JTableView's "model" does all the refreshing for us
        // We use the HighScoresModel's "getTableModel" function to construct
        // the new table model for us
        scoresTable.setModel(model.getTableModel());
    }

    @Override
    public void paintComponent(Graphics g) {
        setBackground(app.colorOf(ColorRole.APP_BACKGROUND));
        super.paintComponent(g);
    }

    // Make the table cells look nice (kind of) using a TableCellRenderer
    class ScoresTableCellRenderer extends JLabel implements TableCellRenderer {

        public ScoresTableCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column)
        {
            setText(value.toString());
            return this;
        }

        @Override
        public void paintComponent(Graphics g) {
            Color textColor  = app.colorOf(ColorRole.TEXT_COLOR);
            Color lightAccent= app.colorOf(ColorRole.LIGHT_ACCENT);
            Color darkAccent = app.colorOf(ColorRole.DARK_ACCENT);

            setForeground(textColor);
            setBackground(lightAccent);
            setFont(new Font("Helvetica", Font.PLAIN, 14));
            EmptyBorder borderPad = new EmptyBorder(new Insets(1,4,1,4));
            Border actualBorder = BorderFactory.createLineBorder(darkAccent,1);
            setBorder(new CompoundBorder(actualBorder, borderPad));

            super.paintComponent(g);
        }
    }
}
