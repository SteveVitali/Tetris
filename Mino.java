import java.awt.Color;
import java.awt.Graphics;

public class Mino {
    private MinoType type;
    protected Color color;
    private int[][] coors;

    // Default constructor instantiates random mino type
    public Mino() {
        this.type  = MinoType.getRandomMinoType();
        this.color = type.getColor();
        this.coors = getCoorCopy(type.getDefaultCoors());
    }

    public void move(int x, int y) {
        for (int[] coor : coors) {
            coor[0] += x;
            coor[1] += y;
        }
    }

    private int[][] getCoorCopy(int[][] someCoors) {
        int[][] copyCoors = new int[4][2];
        for (int i=0; i<someCoors.length; i++) {
            for (int j=0; j<someCoors[i].length; j++) {
                copyCoors[i][j] = someCoors[i][j];
            }
        }
        return copyCoors;
    }

    public int[][] getCoors() {
        return getCoorCopy(coors);
    }

    public void setCoors(int[][] newCoors) {
        this.coors = getCoorCopy(newCoors);
    }

    public Color getColor() {
        return color;
    }

    public void rotateClockwise() {
        rotate(true);
    }

    public void rotateCounterClockwise() {
        rotate(false);
    }

    // TODO: Get I-tetromino rotations working properly
    public void rotate(boolean clockwise) {
        switch (type) {
        case O:
            return;
        default:
            int[][] rotated = new int[4][2];
            int[] origin = coors[1];
            for (int i=0; i<coors.length; i++) {
                int[] coor = coors[i];
                int[] translated = new int[]{
                        coor[0] - origin[0], coor[1] - origin[1]
                };
                translated[1] *= clockwise ? 1 : -1;
                rotated[i] = new int[]{translated[0], translated[1]};
                rotated[i][0] = (int)Math.round(-1*translated[1]);
                rotated[i][1] = (int)Math.round(translated[0]);
                rotated[i][1] *= clockwise ? 1 : -1;
                rotated[i][0] += origin[0];
                rotated[i][1] += origin[1];
            }
            coors = rotated;
        }
    }

    public void draw(Graphics g, int width) {
        for (int[] coor : coors) {
            g.setColor(color);
            g.fillRect(coor[0]*width, coor[1]*width, width, width);

            // Add white/black outline for shadow effect
            g.setColor(Color.white);
            g.drawRect(coor[0]*width, coor[1]*width, width-1, width-1);

            g.setColor(Color.black);
            g.drawRect(coor[0]*width, coor[1]*width, width, width);
        }
    }
}
