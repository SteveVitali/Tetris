import java.util.HashMap;
import java.util.LinkedList;


public class TetrisModel {

    private Mino currentMino;
    private Mino holdMino;
    private MatrixModel matrix;
    private LinkedList<Mino> nextQueue;
    private KeyBinder keyBindings;
    private LinkedList<Action> pendingActions;
    private boolean hitBottom;

    public TetrisModel() {
        keyBindings = new KeyBinder();
        matrix = new MatrixModel();
        matrix.setCurrentMino(new Mino());
        nextQueue = new LinkedList<Mino>();
        pendingActions = new LinkedList<Action>();
        hitBottom = false;
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

    // tryMoveX and tryMoveY are broken up into separate methods
    // to enforce the invariant that in a given move, you can't
    // move in both the X and Y directions, and because we want (slightly)
    // different behavior when a Y move is not allowed versus when
    // an X move isn't
    public void tryMoveX(int dx) {
        if (canMove(dx, 0)) {
            matrix.getCurrentMino().move(dx, 0);
        }
    }

    public void tryMoveY(int dy) {
        if (canMove(0, dy)) {
            matrix.getCurrentMino().move(0, dy);
        } else {
            hitBottom = true;
            System.out.println("HIT BOTTOM");
        }
    }

    private boolean canMove(int dx, int dy) {
        Mino currentMino = getCurrentMino();
        int[][] oldCoors = currentMino.getCoors();
        boolean canMove = true;
        for (int[] coor : oldCoors) {
            if (!matrix.coorEmpty(coor[0]+dx, coor[1]+dy)) {
                canMove = false;
                break;
            }
        }
        return canMove;
    }

    public void hardDrop() {
        System.out.println("Hard drop");
    }

    public void hold() {
        System.out.println("Hold");
    }

    public void rotateClockwise() {
        matrix.getCurrentMino().rotateClockwise();
    }

    public void rotateCounterClockwise() {
        matrix.getCurrentMino().rotateCounterClockwise();
    }
}
