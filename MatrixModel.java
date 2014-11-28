import java.awt.Color;

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
