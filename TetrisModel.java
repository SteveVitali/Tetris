import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

// A TetrisModel contains all of a tetris game's data
public class TetrisModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public final long MINO_LOCK_DELAY = 1000;
    public final int LINES_PER_GAME = 40;
    public final int COUNTDOWN_LENGTH = 3000;
    private long countDownTime;

    private GameStatus status;
    private MinoType holdMino;
    private MatrixModel matrix;
    private QueueModel nextQueue;
    private TimerModel timer;
    private KeyBinder keyBindings;
    private LinkedList<Action> pendingActions;
    private boolean canHold;
    private boolean hasHitWall;
    private TimerTask minoLockTask;

    // Variables to keep track of game statistics
    private int linesCleared;
    private int minoCount;
    HashMap<Integer,Integer> lineClearMap;
    HashMap<Integer,Integer> consecutiveTetrisesMap;
    private int consecutiveTetrises;

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
        hasHitWall = true;
        linesCleared = 0;
        minoCount = 0;
        consecutiveTetrises = 0;
        lineClearMap = new HashMap<Integer,Integer>();
        consecutiveTetrisesMap = new HashMap<Integer,Integer>();
        countDownTime = 0;
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

    // When a key is pressed, check if it's bound to any actions, and if it
    // is, depending on the game state, add that action to the pending actions
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
    public boolean tryMoveX(int dx) {
        Mino currentMino = matrix.getCurrentMino();
        if (matrix.canMove(currentMino, dx, 0)) {
            currentMino.move(dx, 0);
            matrix.updateGhostCoors();
            resetMinoLockDelay();
            return true;
        }
        return false;
    }

    public boolean tryMoveY(int dy) {
        Mino currentMino = matrix.getCurrentMino();
        if (matrix.canMove(currentMino, 0, dy)) {
            currentMino.move(0, dy);
            matrix.updateGhostCoors();
            resetMinoLockDelay();
            return true;
        }
        return false;
    }

    public void checkHitBottom() {
        Mino currentMino = matrix.getCurrentMino();
        if (!matrix.canMove(currentMino, 0, 1)) {
            beginMinoLockDelay();
        }
    }

    public void beginMinoLockDelay() {
        // If the delay hasn't already begun, begin the delay
        // After a time of MINO_LOCK_DELAY has elapsed, if the user has not
        // translated or rotated the mino, it will get locked into the matrix
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
            // Update current mino
            matrix.setCurrentMino(nextQueue.popMino());
            resetMinoLockDelay();

            // Update game statistics
            int numCleared = matrix.clearLines();
            if (numCleared > 0) {
                incrementLineClearsOfSize(numCleared);
            }
            if (numCleared == 4) {
                consecutiveTetrises++;
            } else {
                incrementConsecutiveTetrisData();
                consecutiveTetrises = 0;
            }
            canHold = true;
            minoCount++;
            linesCleared += numCleared;
            // Check if game is win
            if (linesCleared >= LINES_PER_GAME) {
                setStatus(GameStatus.GAME_OVER);
            }
        } else {
            // Failed lock means user has overflows the matrix
            setStatus(GameStatus.GAME_OVER);
        }
    }

    public void hold() {
        // Update hold mino
        // canHold is true if the user has not already tried to hold
        // the current mino
        if (canHold) {
            Mino current = matrix.getCurrentMino();
            // If there's no hold mino, store current mino in hold and
            // set next mino in queue as current mino
            if (holdMino == null) {
                holdMino = current.getType();
                matrix.setCurrentMino(nextQueue.popMino());
            }
            // If there is a hold mino, have the current mino
            // and it swap places
            else {
                MinoType newMinoType = holdMino;
                holdMino = current.getType();
                matrix.setCurrentMino(new Mino(newMinoType));
            }
            // set canHold to false because you can't hold twice in one "turn"
            canHold = false;
            pcs.firePropertyChange("holdMino", null, holdMino);
            resetMinoLockDelay();
        }
    }

    public boolean tryRotateClockwise() {
        Mino current = matrix.getCurrentMino();
        return tryRotate(current.rotateClockwise());
    }

    public boolean tryRotateCounterClockwise() {
        Mino current = matrix.getCurrentMino();
        return tryRotate(current.rotateCounterClockwise());
    }

    public boolean tryRotate(int[][] newCoors) {
        Mino current = matrix.getCurrentMino();
        Point shift = matrix.findClosestValidMinoPlacement(newCoors);
        if (shift != null) {
            current.setCoors(newCoors);
            current.move(shift.x, shift.y);
            matrix.updateGhostCoors();
            resetMinoLockDelay();
            return true;
        }
        return false;
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

    public int getLinesCleared() {
        return linesCleared;
    }

    // getNumberLineClearsOfSize(3) = number of triples, for example
    public int getNumberLineClearsOfSize(int lineClearSize) {
        if (lineClearMap.containsKey(lineClearSize)) {
            return lineClearMap.get(lineClearSize);
        }
        return 0;
    }

    // size = 3 would return the number of "triple tetrises"
    // The method name is confusing, but using this structure saves
    // literally dozens of lines of code and generalizes for streaks of
    // arbitrary size.
    public int getNumberOfStreaksOfConsecutiveTetrisesOfSize(int size) {
        if (consecutiveTetrisesMap.containsKey(size)) {
            return consecutiveTetrisesMap.get(size);
        }
        return 0;
    }

    // Record a new line clear of some size.
    // For example, we call this with size=3 when a triple line clear occurs.
    private void incrementLineClearsOfSize(int size) {
        if (lineClearMap.containsKey(size)) {
            lineClearMap.put(size, lineClearMap.get(size)+1);
        } else {
            lineClearMap.put(size, 1);
        }
    }

    // When a tetris occurs, we call this, and it uses the consecutive
    // tetris counter to record
    private void incrementConsecutiveTetrisData() {
        if (consecutiveTetrises > 0) {
            if (consecutiveTetrisesMap.containsKey(consecutiveTetrises)) {
                int oldValue = consecutiveTetrisesMap.get(consecutiveTetrises);
                consecutiveTetrisesMap.put(consecutiveTetrises, oldValue+1);
            } else {
                consecutiveTetrisesMap.put(consecutiveTetrises, 1);
            }
        }
    }

    public int getMinoCount() {
        return minoCount;
    }

    public boolean getHasHitWall() {
        return hasHitWall;
    }

    public void setHasHitWall(boolean hasHitWall) {
        this.hasHitWall = hasHitWall;
    }

    public long getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(long time) {
        countDownTime = time;
    }

    // Convert long time to an integer time for countdown timer to display
    public int getCountDownTimeInt() {
        return (int) Math.ceil((double) countDownTime/1000);
    }
}
