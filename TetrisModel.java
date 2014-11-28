import java.util.HashMap;
import java.util.LinkedList;


public class TetrisModel {

    private Mino currentMino;
    private Mino holdMino;
    private MatrixModel matrix;
    private LinkedList<Mino> nextQueue;
    private KeyBinder keyBindings;
    private LinkedList<Action> pendingActions;

    public TetrisModel() {
        keyBindings = new KeyBinder();
        matrix = new MatrixModel();
        matrix.setCurrentMino(new Mino());
        nextQueue = new LinkedList<Mino>();
        pendingActions = new LinkedList<Action>();
        populateNextQueue();
    }

    private void populateNextQueue() {
        for (int i=0; i<5; i++) {
            nextQueue.push(new Mino());
        }
    }

    public MatrixModel getMatrix() {
        return matrix;
    }

    public void setKeyPressed(int keyCode) {
        if (keyBindings.hasBinding(keyCode)) {
            Action newAction = keyBindings.actionForKey(keyCode);
            pendingActions.add(newAction);
        }
    }

    public Action popAction() {
        if (!pendingActions.isEmpty()) {
            return pendingActions.pop();
        }
        return null;
    }

    public void addAction(Action action) {
        pendingActions.add(action);
    }

    public Mino getCurrentMino() {
        return matrix.getCurrentMino();
    }

    public void tryMove(int x, int y) {

    }

    public void hardDrop() {
        System.out.println("Hard drop");
    }

    public void hold() {
        System.out.println("Hold");
    }

    public void rotateClockwise() {
        System.out.println("Rotate clockwise");
    }

    public void rotateCounterClockwise() {
        System.out.println("Rotate counter clockwise");
    }
}
