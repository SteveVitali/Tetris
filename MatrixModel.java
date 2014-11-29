import java.awt.Color;
import java.util.ArrayList;

public class MatrixModel {

    private int width  = 10;
    private int height = 20;

    private Color[][] colors;
    private Mino currentMino;
    private Ghost ghost;

    public MatrixModel() {
        initialize();
    }

    public MatrixModel(int width, int height) {
        this.width  = width;
        this.height = height;
        initialize();
    }

    public void initialize() {
        this.colors = new Color[width][height];
    }

    public Color colorAtCoor(int x, int y) {
        if (inBounds(x, y)) {
            return colors[x][y];
        } else {
            return null;
        }
    }

    public boolean coorEmpty(int x, int y) {
        if (inBounds(x, y)) {
            return colors[x][y] == null ? true : false;
        } else if (inXBounds(x) && y < 20) {
            return true;
        }
        return false;
    }

    public boolean canMove(Mino mino, int dx, int dy) {
        int[][] oldCoors = mino.getCoors();
        boolean canMove = true;
        for (int[] coor : oldCoors) {
            if (!coorEmpty(coor[0]+dx, coor[1]+dy)) {
                canMove = false;
                break;
            }
        }
        return canMove;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setCurrentMino(Mino mino) {
        currentMino = mino;
        ghost = new Ghost(mino);
        updateGhostCoors();
    }

    public void hardDropCurrentMino() {
        while (canMove(currentMino, 0, 1)) {
            currentMino.move(0, 1);
        }
    }

    public void lockMinoIntoMatrix() {
        Color color = currentMino.getColor();
        for (int[] coor : currentMino.getCoors()) {
            int x = coor[0];
            int y = coor[1];
            this.colors[x][y] = color;
        }
    }

    // Clear lines and return number cleared
    public int clearLines() {
        ArrayList<Integer> rowsToRemove = new ArrayList<Integer>();
        for (int y = 0; y < height; y++) {
            boolean isFull = true;
            for (int x = 0; x < width; x++) {
                if (colors[x][y] == null) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                rowsToRemove.add(y);
            }
        }
        removeRows(rowsToRemove);
        updateGhostCoors();
        return rowsToRemove.size();
    }

    public void removeRows(ArrayList<Integer> rowsToRemove) {
        Color[][] newColors = new Color[10][20];
        for (int y = 0; y < height; y++) {
            if (!rowsToRemove.contains(y)) {
                int shiftDown = numValuesGreaterThan(rowsToRemove, y);
                for (int x = 0; x < width; x++) {
                    newColors[x][y+shiftDown] = colors[x][y];
                }
            }
        }
        this.colors = newColors;
    }

    private int numValuesGreaterThan(ArrayList<Integer> values, int compare) {
        int numValuesGreaterThan = 0;
        for (int value : values) {
            if (value > compare) {
                numValuesGreaterThan++;
            }
        }
        return numValuesGreaterThan;
    }

    public Mino getCurrentMino() {
        return currentMino;
    }

    public Ghost getGhost() {
        return ghost;
    }

    public void updateGhostCoors() {
        ghost.setCoors(currentMino.getCoors());
        while (canMove(ghost, 0,1)) {
            ghost.move(0, 1);
        }
    }

    private boolean inBounds(int x, int y) {
        return (x > -1  && x < width &&
                y > -1 && y < height );
    }

    private boolean inXBounds(int x) {
        return (x > -1 && x < width);
    }
}
