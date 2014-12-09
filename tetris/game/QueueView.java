package tetris.game;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class QueueView extends JPanel {

    private int WIDTH  = 128;
    private int HEIGHT = 512;

    private QueueModel model;
    private MinoPanel[] minoPanels;

    private AppController app;

    public QueueView(AppController a) {
        this.app = a;
    }

    public QueueView(QueueModel m) {
        setModel(m);
    }

    public void setModel(QueueModel m) {
        this.model = m;
        this.minoPanels = new MinoPanel[model.getSize()];

        this.removeAll();
        setOpaque(false);

        for (int i = 0; i < minoPanels.length; i++) {
            minoPanels[i] = new MinoPanel(app);
            this.add(minoPanels[i]);
        }
        updateMinoPanels();
        revalidate();
        repaint();
    }

    public void updateMinoPanels() {
        for (int i = 0; i < minoPanels.length; i++) {
            minoPanels[i].setMino(model.getMino(i));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
