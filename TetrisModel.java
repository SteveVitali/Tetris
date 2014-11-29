import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class TetrisModel {

    private Mino holdMino;
    private MatrixModel matrix;
    private QueueModel nextQueue;
    private KeyBinder keyBindings;
    private LinkedList<Action> pendingActions;
    private boolean hitBottom;

    public TetrisModel() {
        keyBindings = new KeyBinder();
        matrix = new MatrixModel();
        nextQueue = new QueueModel(5);
        matrix.setCurrentMino(new Mino());
        pendingActions = new LinkedList<Action>();
        hitBottom = false;
    }

    public MatrixModel getMatrix() {
        return matrix;
    }

    public QueueModel getQueue() {
        return nextQueue;
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

    // tryMoveX and tryMoveY are broken up into separate methods
    // to enforce the invariant that in a given move, you can't
    // move in both the X and Y directions, and because we want (slightly)
    // different behavior when a Y move is not allowed versus when
    // an X move isn't
    public void tryMoveX(int dx) {
        Mino currentMino = matrix.getCurrentMino();
        if (matrix.canMove(currentMino, dx, 0)) {
            currentMino.move(dx, 0);
            matrix.updateGhostCoors();
        }
    }

    public void tryMoveY(int dy) {
        Mino currentMino = matrix.getCurrentMino();
        if (matrix.canMove(currentMino, 0, dy)) {
            currentMino.move(0, dy);
            matrix.updateGhostCoors();
        } else {
            hitBottom = true;
            System.out.println("HIT BOTTOM");
        }
    }

    public void hardDrop() {
        matrix.hardDropCurrentMino();
        lockMinoIntoMatrix();
    }

    public void lockMinoIntoMatrix() {
        matrix.lockMinoIntoMatrix();
        matrix.setCurrentMino(nextQueue.popMino());
        int linesClared = matrix.clearLines();
    }

    public void hold() {
        System.out.println("Hold");
    }

    public void rotateClockwise() {
        matrix.getCurrentMino().rotateClockwise();
        matrix.updateGhostCoors();
    }

    public void rotateCounterClockwise() {
        matrix.getCurrentMino().rotateCounterClockwise();
        matrix.updateGhostCoors();
    }
}
