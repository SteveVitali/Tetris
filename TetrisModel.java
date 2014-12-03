import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class TetrisModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final long MINO_LOCK_DELAY = 1000;
    private GameStatus status;
    private MinoType holdMino;
    private MatrixModel matrix;
    private QueueModel nextQueue;
    private TimerModel timer;
    private KeyBinder keyBindings;
    private LinkedList<Action> pendingActions;
    private boolean canHold;
    private TimerTask minoLockTask;
    private int linesCleared;

    public TetrisModel() {
        keyBindings = new KeyBinder();
        initializeGameData();
    }

    public void initializeGameData() {
        matrix = new MatrixModel();
        nextQueue = new QueueModel(5);
        matrix.setCurrentMino(new Mino());
        timer = new TimerModel();
        pendingActions = new LinkedList<Action>();
        canHold = true;
        linesCleared = 0;
        setStatus(GameStatus.BEFORE_GAME);
    }

    public MatrixModel getMatrix() {
        return matrix;
    }

    public QueueModel getQueue() {
        return nextQueue;
    }

    public TimerModel getTimer() {
        return timer;
    }

    public void setKeyPressed(int keyCode) {
        if (keyBindings.hasBinding(keyCode)) {
            switch (status) {
            case PLAYING:
                Action newAction = keyBindings.actionForKey(keyCode);
                addAction(newAction);
                break;
            case PAUSED:
                if (keyCode == keyBindings.keyForAction(Action.TOGGLE_PAUSE)) {
                    addAction(Action.TOGGLE_PAUSE);
                }
                break;
            default:
                break;
            }

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
        boolean success = matrix.lockMinoIntoMatrix();
        if (success) {
            matrix.setCurrentMino(nextQueue.popMino());
            resetMinoLockDelay();
            linesCleared += matrix.clearLines();
            canHold = true;
        } else {
            setStatus(GameStatus.GAME_OVER);
        }
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

    public void setTime(long time) {
        timer.updateTime(time);
    }

    public void addTime(long time) {
        timer.addTime(time);
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public void setLinesCleared(int linesCleared) {
        this.linesCleared = linesCleared;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        pcs.firePropertyChange("status", this.status, status);
        this.status = status;
    }

    public long getFinalTime() {
        return timer.getTime();
    }
}
