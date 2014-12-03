import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class HighScoresView extends JPanel {

    private HighScoresModel model;
    private JTable scoresTable;

    private String[] columnNames = {"User", "Score"};

    public HighScoresView(HighScoresModel model) {
        this.model = model;

        setBackground(Color.black);

        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("scores")) {
                    refreshTable();
                }
            }
        });
    }

    public void refreshTable() {
        scoresTable = new JTable(model.getNamesAndScoresArray(), columnNames);
        JScrollPane scrollPane = new JScrollPane(scoresTable);
        scrollPane.setOpaque(false);
        scrollPane.setBackground(Color.black);
        add(scrollPane);
        revalidate();
        repaint();
    }
}
