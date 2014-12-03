import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

public class HighScoresModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private ArrayList<Entry<String,Long>> scores;

    public HighScoresModel() {
        scores = new ArrayList<Entry<String,Long>>();
    }

    public void addScore(String name, long time) {
        scores.add(createScore(name, time));
        pcs.firePropertyChange("scores", null, null);
    }

    private SimpleEntry<String,Long> createScore(String name, long time) {
        return new AbstractMap.SimpleEntry<String,Long>(name, time);
    }

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
