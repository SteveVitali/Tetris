import java.awt.Color;
import java.awt.Graphics;

public class Mino {
    private MinoType type;
    private Color color;
    private int[][] coors;
    private boolean active;

    // Default constructor instantiates random mino type
    public Mino() {
        this.type  = MinoType.getRandomMinoType();
        this.color = type.getColor();
        this.coors = type.getDefaultCoors();
        this.active = false;
    }

    public void move(int x, int y) {
        for (int[] coor : coors) {
            coor[0] += x;
            coor[1] += y;
        }
    }

    public int[][] getCoors() {
        int[][] copyCoors = new int[4][2];
        for (int i=0; i<4; i++) {
            copyCoors[i][0] = coors[i][0];
            copyCoors[i][1] = coors[i][1];
        }
        return copyCoors;
    }

    public void rotateClockwise() {
        rotate(true);
    }

    public void rotateCounterClockwise() {
        rotate(false);
    }

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

    public int[] getOrigin() {
        switch (type) {
        case I:
            return new int[]{coors[2][0], coors[2][1]+1};
        default:
            return coors[1];
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
