package tetris.game;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import tetris.utilities.TetrisUIPanel;

@SuppressWarnings("serial")
public class HighScoresView extends TetrisUIPanel {

    private HighScoresModel model;
    private JTable scoresTable;

    public HighScoresView(HighScoresModel model) {
        this.model = model;

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
        scoresTable.setModel(model.getTableModel());
    }

    class ScoresTableCellRenderer extends JLabel implements TableCellRenderer {

        public ScoresTableCellRenderer() {
            setOpaque(true);
            setForeground(Color.black);
            setBackground(new Color(236,236,236));
            setFont(new Font("Helvetica", Font.PLAIN, 14));
            EmptyBorder borderPad = new EmptyBorder(new Insets(1,4,1,4));
            Border actualBorder = BorderFactory.createLineBorder(new Color(206,206,206),1);
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
