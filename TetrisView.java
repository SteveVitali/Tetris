import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TetrisView extends JPanel {

    private int WIDTH = 800;
    private int HEIGHT= 512;

    private TetrisModel model;
    private TetrisController controller;
    private MatrixView matrix;
    private QueueView queue;
    private MinoPanel hold;

    public TetrisView(TetrisModel m) {
        this.model = m;

        MatrixModel matrixModel = model.getMatrix();
        QueueModel  queueModel  = model.getQueue();

        this.matrix = new MatrixView(matrixModel);
        this.queue  = new QueueView (queueModel);
        this.hold   = new MinoPanel();

        queueModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("queue")) {
                    queue.updateMinoPanels();
                }
            }
        });

        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("holdMino")) {
                    hold.setMino(new Mino((MinoType)e.getNewValue()));
                }
            }
        });

        add(hold  , BorderLayout.WEST);
        add(matrix, BorderLayout.CENTER);
        add(queue , BorderLayout.EAST);

        setLayout( new FlowLayout() );
        addKeyListener(new KeyHandler(model));
        setFocusable(true);
        setBackground(Color.black);
    }

    public void setController(TetrisController c) {
        this.controller = c;
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
