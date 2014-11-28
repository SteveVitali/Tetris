import java.awt.Color;
import java.awt.Graphics;

public class Ghost extends Mino {

    public Ghost(Mino mino) {
        Color c = mino.getColor();
        this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
    }

    @Override
    public void draw(Graphics g, int width) {
        for (int[] coor : super.getCoors()) {
            g.setColor(color);
            g.fillRect(coor[0]*width+1, coor[1]*width+1, width-2, width-2);
            g.setColor(Color.black);
            g.drawRect(coor[0]*width+1, coor[1]*width+1, width-3, width-3);
        }
    }
}
