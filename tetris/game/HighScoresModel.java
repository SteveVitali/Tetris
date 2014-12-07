package tetris.game;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

public class HighScoresModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ArrayList<Entry<String,Long>> scores;

    public HighScoresModel() {
        scores = new ArrayList<Entry<String,Long>>();
    }

    @SuppressWarnings("serial")
    public DefaultTableModel getTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Class getColumnClass(int column) {
                int row = 0;
                Object o = getValueAt(row, column);
                if (o == null) {
                    return Object.class;
                } else {
                    return o.getClass();
                }
            }
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tableModel.addColumn("Name", getNames());
        tableModel.addColumn("Time", getTimes());
        return tableModel;
    }

    public String[] getNames() {
        String[] names = new String[scores.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = scores.get(i).getKey();
        }
        return names;
    }

    public String[] getTimes() {
        String[] times = new String[scores.size()];
        for (int i = 0; i < times.length; i++) {
            long time = scores.get(i).getValue();
            times[i] = TimerModel.getTimeString(time);
        }
        return times;
    }

    public void addScore(String name, long time) {
        scores.add(createScore(name, time));
        pcs.firePropertyChange("scores", null, null);
    }

    private SimpleEntry<String,Long> createScore(String name, long time) {
        return new AbstractMap.SimpleEntry<String,Long>(name, time);
    }

    // This is old code from when we weren't using a table model
    public String[][] getNamesAndScoresArray() {
        String[][] data = new String[scores.size()][2];
        for (int i = 0; i < scores.size(); i++) {
            Entry<String,Long> score = scores.get(i);
            data[i][0] = score.getKey();
            data[i][1] = TimerModel.getTimeString(score.getValue());
        }
        return data;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
}
