
import java.awt.Dimension;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class MinoPanel extends GameElementPanel {

    int WIDTH = 84;
    int UNIT  = 16;

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