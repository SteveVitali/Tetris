import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TetrisView extends JPanel {

    private AppController app;
    private TetrisModel model;
    @SuppressWarnings("unused")
    private TetrisController controller;
    private MatrixView matrix;
    private QueueView queue;
    private TimerView timer;
    private MinoPanel hold;

    public TetrisView(AppController app) {
        this.app = app;

        this.matrix = new MatrixView();
        this.queue  = new QueueView ();
        this.timer  = new TimerView ();
        this.hold   = new MinoPanel();

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
        addKeyListener(new KeyHandler());
        setFocusable(true);
        setBackground(AppController.BG_COLOR);
    }

    public void setModel(TetrisModel m) {
        this.model = m;

        MatrixModel matrixModel = model.getMatrix();
        QueueModel  queueModel  = model.getQueue();
        TimerModel  timerModel  = model.getTimer();

        matrix.setModel(matrixModel);
        queue.setModel(queueModel);
        timer.setModel(timerModel);

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
        return new Dimension(app.WIDTH, app.HEIGHT);
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            model.setKeyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}
