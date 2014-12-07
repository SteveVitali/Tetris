package tetris.game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MatrixView extends JPanel {

    private int WIDTH = 250;
    private int HEIGHT= 2 * WIDTH;
    private int UNIT  = WIDTH / 10;

    private MatrixModel model;

    public MatrixView() {}

    public MatrixView(MatrixModel m) {
        setModel(m);
    }

    public void setModel(MatrixModel m) {
        this.model = m;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y=0; y<model.getHeight(); y++) {
            for (int x=0; x<model.getWidth(); x++) {
                // Draw background tile
                if ((x+y) % 2 == 0) {
                    g.setColor(new Color(40,40,40,128));
                } else {
                    g.setColor(new Color(50,50,50,128));
                }
                g.fillRoundRect(x*UNIT, y*UNIT, UNIT+1, UNIT+1, 4, 4);

                // Draw tile if not null
                Color colorAtCoor = model.colorAtCoor(x, y);
                if (colorAtCoor != null) {
                    g.setColor(colorAtCoor);
                    g.fillRect(x*UNIT, y*UNIT, UNIT, UNIT);

                    // Add white/black outline for shadow effect
                    g.setColor(Color.white);
                    g.drawRect(x*UNIT, y*UNIT, UNIT-1, UNIT-1);

                    g.setColor(Color.black);
                    g.drawRect(x*UNIT, y*UNIT, UNIT, UNIT);
                }

            }
        }
        model.getCurrentMino().draw(g, UNIT);
        model.getGhost().draw(g, UNIT);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}