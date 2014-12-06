package tetris.game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

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

    public GameStatisticsView(AppController a) {
        this.app = a;

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
        timeLabel.setForeground(Color.white);

        TetrisUIPanel statsTable= new TetrisUIPanel();
        TetrisUIPanel statsContainer = new TetrisUIPanel();

        statsTable.setPreferredSize(new Dimension(560, 512));
        statsContainer.setPreferredSize(new Dimension(700, 260));

        stats = new HashMap<String,String>();
        statsTableCells = new HashMap<String,StatsTableCell>();

        for (String prop : statProperties) {
            String statTitle = prop.substring(0,1).toUpperCase()+prop.substring(1);
            stats.put(prop, "");
            statsTableCells.put(prop, new StatsTableCell(statTitle, ""));
            statsTable.add(statsTableCells.get(prop));
        }
        statsContainer.add(statsTable);
        add(timeLabel);
        add(statsContainer);
    }

    public void setModel(TetrisModel m) {
        this.model = m;
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

            Border in = BorderFactory.createEmptyBorder(2, 2, 2, 2);
            Border out = BorderFactory.createLineBorder(new Color(36,36,36));
            setBorder(BorderFactory.createCompoundBorder(out, in));
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
