
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.text.DecimalFormat;

@SuppressWarnings("serial")
public class GameStatisticsView extends JPanel {

    private TetrisModel model;
    private AppController app;
    private HashMap<String,String> stats;
    private HashMap<String,StatsTableCell> statsTableCells;
    private long time;
    private JLabel timeLabel;
    // List of statistics properties for easy iteration over hashmap
    private String[] statProperties = {
            "minos", "minos per minute",
            "lines", "lines per minute",
            "singles", "doubles", "triples",
            "single tetrises", "double tetrises",
            "triple tetrises"
    };
    private JTextField nameField;
    private JButton submitButton;
    private JButton doneButton;
    private JPanel statsTable;
    private JPanel statsContainer;

    public GameStatisticsView(AppController a) {
        this.app = a;

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));

        statsTable = new JPanel();
        statsContainer = new JPanel();

        statsTable.setPreferredSize(new Dimension(700, 200));

        stats = new HashMap<String,String>();
        statsTableCells = new HashMap<String,StatsTableCell>();

        // Add all the "table cells"; initialize them with their 'property'
        // title but no data
        for (String prop : statProperties) {
            String statTitle = prop.substring(0,1).toUpperCase()+prop.substring(1);
            stats.put(prop, "");
            statsTableCells.put(prop, new StatsTableCell(statTitle, ""));
            statsTable.add(statsTableCells.get(prop));
        }
        statsContainer.add(statsTable);

        // Add score submission components
        nameField = new JTextField(18);
        nameField.setFont(new Font("Helvetica", Font.PLAIN, 14));
        submitButton = new JButton("Submit Score");
        doneButton = new JButton("Done");

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setPreferredSize(new Dimension(700,520));
        containerPanel.add(timeLabel);
        containerPanel.add(statsContainer);
        containerPanel.add(nameField);
        containerPanel.add(submitButton);
        containerPanel.add(doneButton);

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHome();
            }
        });
        add(containerPanel);
    }

    public void setModel(TetrisModel m) {
        this.model = m;
        // Hook up submit button action listener
        // The first if is there to make sure the
        // setModel method is idempotent
        if (submitButton.getActionListeners().length == 0) {
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText().trim();
                    if (!name.equals("")) {
                        app.submitScore(name, model.getFinalTime());
                    }
                }
            });
        }
    }

    // This does all the calculations to populate the "table cells" with
    // their actual game data; it's called whenever a game has just ended
    public void refreshData() {
        time = model.getFinalTime();

        DecimalFormat twoDecimals = new DecimalFormat("#.##");

        double minutes = ((double)time / 1000 / 60);
        double linesPerMin = model.getLinesCleared() / minutes;
        double lpmFormatted = Double.valueOf(twoDecimals.format(linesPerMin));

        double minosPerMin = model.getMinoCount() / minutes;
        double mpmFormatted = Double.valueOf(twoDecimals.format(minosPerMin));

        stats.put("lines", ""+model.getLinesCleared());
        stats.put("lines per minute", ""+lpmFormatted);
        stats.put("minos", ""+model.getMinoCount());
        stats.put("minos per minute", ""+mpmFormatted);
        stats.put("minos", ""+model.getMinoCount());
        stats.put("singles", ""+model.getNumberLineClearsOfSize(1));
        stats.put("doubles", ""+model.getNumberLineClearsOfSize(2));
        stats.put("triples", ""+model.getNumberLineClearsOfSize(3));
        stats.put("single tetrises", ""+model.getNumberOfStreaksOfConsecutiveTetrisesOfSize(1));
        stats.put("double tetrises", ""+model.getNumberOfStreaksOfConsecutiveTetrisesOfSize(2));
        stats.put("triple tetrises", ""+model.getNumberOfStreaksOfConsecutiveTetrisesOfSize(3));

        // Update the stats labels
        for (Entry<String,StatsTableCell> entry : statsTableCells.entrySet()) {
            String newValue = stats.get(entry.getKey());
            entry.getValue().setValue(newValue);
        }

        timeLabel.setText("Time: "+TimerModel.getTimeString(time));

        boolean isWin = (model.getLinesCleared() >= model.LINES_PER_GAME);
        nameField.setVisible(isWin);
        submitButton.setVisible(isWin);
        doneButton.setVisible(!isWin);

        revalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Color bgColor = app.colorOf(ColorRole.APP_BACKGROUND);
        statsContainer.setBackground(bgColor);
        statsTable.setBackground(bgColor);
        timeLabel.setForeground(app.colorOf(ColorRole.TEXT_COLOR));
        this.setBackground(bgColor);
        super.paintComponent(g);
    }

    // This is a helper class for displaying JPanels with key and
    // value labels on the WEST and EAST sides; used as "table cell"
    private class StatsTableCell extends JPanel {
        JLabel keyLabel;
        JLabel valueLabel;

        public StatsTableCell(String key, String value) {
            keyLabel = new TetrisUILabel(app, key);
            valueLabel = new TetrisUILabel(app, value);

            setLayout(new BorderLayout());
            add(keyLabel,BorderLayout.WEST);
            add(valueLabel,BorderLayout.EAST);

            setKey(key);
            setValue(value);
        }

        public void setKey(String key) {
            keyLabel.setText(key);
        }

        public void setValue(String value) {
            valueLabel.setText(value);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(256, 32);
        }

        @Override
        public void paintComponent(Graphics g) {
            Color darkAccent = app.colorOf(ColorRole.DARK_ACCENT);
            Color lightAccent= app.colorOf(ColorRole.LIGHT_ACCENT);
            Border in = BorderFactory.createEmptyBorder(4, 8, 4, 8);
            Border out = BorderFactory.createLineBorder(darkAccent);
            setBorder(BorderFactory.createCompoundBorder(out, in));
            setBackground(lightAccent);
            super.paintComponent(g);
        }
    }
}
