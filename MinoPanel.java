import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MinoPanel extends GameElementPanel {

    int WIDTH = 84;
    int UNIT  = 16;

    private Mino mino;

    public MinoPanel() {
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
