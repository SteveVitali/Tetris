import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TetrisView extends JPanel {

    private int WIDTH = 800;
    private int HEIGHT= 512;
    private int TENTH_SEC = 100;

    private TetrisModel model;
    private TetrisController controller;
    private MatrixView matrix;
    private QueueView queue;
    private TimerView timer;
    private MinoPanel hold;

    public TetrisView(TetrisModel m) {
        this.model = m;

        MatrixModel matrixModel = model.getMatrix();
        QueueModel  queueModel  = model.getQueue();
        TimerModel  timerModel  = model.getTimer();

        this.matrix = new MatrixView(matrixModel);
        this.queue  = new QueueView (queueModel);
        this.timer  = new TimerView (timerModel);
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

        JPanel west = new JPanel();
        west.setLayout(new FlowLayout());
        west.setPreferredSize(new Dimension(100, 512));
        west.setOpaque(false);
        west.add(hold);
        west.add(timer);
        add(west  , BorderLayout.WEST);
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
