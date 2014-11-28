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
