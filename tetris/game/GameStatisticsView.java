package tetris.game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.text.DecimalFormat;
import tetris.utilities.TetrisUILabel;
import tetris.utilities.TetrisUIPanel;

@SuppressWarnings("serial")
public class GameStatisticsView extends TetrisUIPanel {

    private TetrisModel model;
    private AppController app;
    private HashMap<String,String> stats;
    private HashMap<String,StatsTableCell> statsTableCells;
    private long time;
    private JLabel timeLabel;
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

    public GameStatisticsView(AppController a) {
        this.app = a;

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        timeLabel.setForeground(Color.black);

        TetrisUIPanel statsTable= new TetrisUIPanel();
        TetrisUIPanel statsContainer = new TetrisUIPanel();

        statsTable.setPreferredSize(new Dimension(700, 200));

        stats = new HashMap<String,String>();
        statsTableCells = new HashMap<String,StatsTableCell>();

        for (String prop : statProperties) {
            String statTitle = prop.substring(0,1).toUpperCase()+prop.substring(1);
            stats.put(prop, "");
            statsTableCells.put(prop, new StatsTableCell(statTitle, ""));
            statsTable.add(statsTableCells.get(prop));
        }
        statsContainer.add(statsTable);

        nameField = new JTextField(18);
        nameField.setFont(new Font("Helvetica", Font.PLAIN, 14));
        submitButton = new JButton("Submit Score");
        doneButton = new JButton("Done");

        TetrisUIPanel containerPanel = new TetrisUIPanel();
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

    private class StatsTableCell extends TetrisUIPanel {
        JLabel keyLabel;
        JLabel valueLabel;

        public StatsTableCell(String key, String value) {
            keyLabel = new TetrisUILabel(key);
            valueLabel = new TetrisUILabel(value);

            setLayout(new BorderLayout());
            add(keyLabel,BorderLayout.WEST);
            add(valueLabel,BorderLayout.EAST);

            setKey(key);
            setValue(value);

            Border in = BorderFactory.createEmptyBorder(4, 8, 4, 8);
            Border out = BorderFactory.createLineBorder(new Color(206,206,206));
            setBorder(BorderFactory.createCompoundBorder(out, in));
            setBackground(new Color(236,236,236));
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
    }
}
