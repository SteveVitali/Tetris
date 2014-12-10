import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// The MatrixModel contains all the data associated with the tetris matrix
public class MatrixModel {

    private int width  = 10;
    private int height = 20;

    // colors is an array holding the color of each block in the matrix
    private Color[][] colors;
    private Mino currentMino;
    private Ghost ghost;

    private ArrayList<Point> validShifts;

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
        constructValidShifts();
    }

    // See "findClosestValidMinoPlacement(int[][] coors)" for why
    // this is useful.
    public void constructValidShifts() {
        this.validShifts = new ArrayList<Point>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 0; j++) {
                validShifts.add(new Point(i, j));
            }
        }
        Collections.sort(validShifts, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                int d1 = Math.abs(p1.x) + Math.abs(p1.y);
                int d2 = Math.abs(p2.x) + Math.abs(p2.y);
                if (d1 > d2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
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
        return canMove(mino.getCoors(), dx, dy);
    }

    public boolean canMove(int[][] coors, int dx, int dy) {
        boolean canMove = true;
        for (int[] coor : coors) {
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

    // Return true if mino was successfully locked into the matrix
    public boolean lockMinoIntoMatrix() {
        boolean success = true;
        try {
            Color color = currentMino.getColor();
            for (int[] coor : currentMino.getCoors()) {
                int x = coor[0];
                int y = coor[1];
                this.colors[x][y] = color;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            success = false;
        }
        return success;
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

    // Helper function used in removeRows that returns the number of values
    // in an ArrayList of integers that are greater than some comparison int
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

    public Point findClosestValidMinoPlacement(int[][] coors) {
        // validShifts is sorted in order of distance, so this will
        // either return the smallest shift that is <= 2 in x and y
        // direction, or it will return null and abort the rotation
        for (Point point : validShifts) {
            if (canMove(coors, point.x, point.y)) {
                return point;
            }
        }
        return null;
    }
}
