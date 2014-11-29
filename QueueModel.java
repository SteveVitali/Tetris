import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;


public class QueueModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private LinkedList<Mino> queue;
    private int queueSize;

    public QueueModel(int s) {
        queueSize = s;
        queue = new LinkedList<Mino>();
        for (int i=0; i<queueSize; i++) {
            queue.push(new Mino());
        }
    }

    public Mino popMino() {
        Mino popped = queue.pop();
        queue.add(new Mino());
        System.out.println("it should fire now");
        pcs.firePropertyChange("queue", null, null);
        return popped;
    }

    public Mino getMino(int i) {
        return queue.get(i);
    }

    public ArrayList<Mino> getNextMinos() {
        ArrayList<Mino> minos = new ArrayList<Mino>();
        for (Mino mino : queue) {
            minos.add(mino);
        }
        return minos;
    }

    public int getSize() {
        return queue.size();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
}
