
import java.awt.Dimension;
import java.awt.Graphics;

@SuppressWarnings("serial")
// This is used in the hold queue and the next queue to draw
// the minos centered inside of GameElementPanels
public class MinoPanel extends GameElementPanel {

    public static int WIDTH = 84;
    public static int UNIT  = 16;

    private Mino mino;

    public MinoPanel(AppController c) {
        super(c);
    }

    public void setMino(Mino mino) {
        this.mino = mino;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mino != null) {
            mino.drawInRect(g, WIDTH, WIDTH);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, WIDTH);
    }
}
