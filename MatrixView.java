import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MatrixView extends JPanel {

    private int WIDTH = 250;
    private int HEIGHT= 2 * WIDTH;
    private int UNIT  = WIDTH / 10;

    private MatrixModel model;

    public MatrixView(MatrixModel m) {
        this.model = m;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y=0; y<model.getHeight(); y++) {
            for (int x=0; x<model.getWidth(); x++) {
                g.setColor(model.colorAtCoor(x, y));
                g.drawRect(x*UNIT, y*UNIT, UNIT, UNIT);
            }
        }
        model.getCurrentMino().draw(g, UNIT);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
