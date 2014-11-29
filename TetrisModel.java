import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class TetrisModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final long MINO_LOCK_DELAY = 1000;
    private MinoType holdMino;
    private MatrixModel matrix;
    private QueueModel nextQueue;
    private KeyBinder keyBindings;
    private LinkedList<Action> pendingActions;
    private boolean canHold;
    private TimerTask minoLockTask;

    public TetrisModel() {
        keyBindings = new KeyBinder();
        matrix = new MatrixModel();
        nextQueue = new QueueModel(5);
        matrix.setCurrentMino(new Mino());
        pendingActions = new LinkedList<Action>();
        canHold = true;
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
            resetMinoLockDelay();
        }
    }

    public void tryMoveY(int dy) {
        Mino currentMino = matrix.getCurrentMino();
        if (matrix.canMove(currentMino, 0, dy)) {
            currentMino.move(0, dy);
            matrix.updateGhostCoors();
            resetMinoLockDelay();
        }
    }

    public void checkHitBottom() {
        Mino currentMino = matrix.getCurrentMino();
        if (!matrix.canMove(currentMino, 0, 1)) {
            beginMinoLockDelay();
            System.out.println("HIT BOTTOM");
        }
    }

    public void beginMinoLockDelay() {
        if (minoLockTask == null) {
            minoLockTask = new TimerTask() {
                @Override
                public void run() {
                    pendingActions.push(Action.LOCK_MINO);
                }
            };
            (new Timer()).schedule(minoLockTask, MINO_LOCK_DELAY);
        }
    }

    public void resetMinoLockDelay() {
        if (minoLockTask != null) {
            minoLockTask.cancel();
            minoLockTask = null;
        }
    }

    public void hardDrop() {
        matrix.hardDropCurrentMino();
        lockMinoIntoMatrix();
    }

    public void lockMinoIntoMatrix() {
        matrix.lockMinoIntoMatrix();
        matrix.setCurrentMino(nextQueue.popMino());
        resetMinoLockDelay();
        int linesClared = matrix.clearLines();
        canHold = true;
    }

    public void hold() {
        if (canHold) {
            Mino current = matrix.getCurrentMino();
            if (holdMino == null) {
                holdMino = current.getType();
                matrix.setCurrentMino(nextQueue.popMino());
            } else {
                MinoType newMinoType = holdMino;
                holdMino = current.getType();
                matrix.setCurrentMino(new Mino(newMinoType));
            }
            canHold = false;
            pcs.firePropertyChange("holdMino", null, holdMino);
            resetMinoLockDelay();
        }
    }

    public void tryRotateClockwise() {
        Mino current = matrix.getCurrentMino();
        tryRotate(current.rotateClockwise());
    }

    public void tryRotateCounterClockwise() {
        Mino current = matrix.getCurrentMino();
        tryRotate(current.rotateCounterClockwise());
    }

    public void tryRotate(int[][] newCoors) {
        Mino current = matrix.getCurrentMino();
        Point shift = matrix.findClosestValidMinoPlacement(newCoors);
        if (shift != null) {
            current.setCoors(newCoors);
            current.move(shift.x, shift.y);
            matrix.updateGhostCoors();
            resetMinoLockDelay();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
}
