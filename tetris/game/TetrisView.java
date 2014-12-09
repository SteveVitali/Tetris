package tetris.game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tetris.utilities.ColorRole;
import tetris.utilities.GameElementPanel;

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
    private MinoCountPanel minoCountPanel;

    public TetrisView(AppController app) {
        this.app = app;

        this.matrix = new MatrixView(app);
        this.queue  = new QueueView (app);
        this.timer  = new TimerView (app);
        this.hold   = new MinoPanel(app);

        this.minoCountPanel = new MinoCountPanel();

        JPanel west = new JPanel();
        west.setPreferredSize(new Dimension(100, 512));
        west.setOpaque(false);
        west.add(hold);
        west.add(timer);
        west.add(minoCountPanel);

        JPanel east = new JPanel();
        east.setPreferredSize(new Dimension(100, 512));
        east.setOpaque(false);
        east.add(queue);

        add(west  , BorderLayout.WEST);
        add(matrix, BorderLayout.CENTER);
        add(east , BorderLayout.EAST);

        addKeyListener(new KeyHandler());
        setFocusable(true);
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
        setBackground(app.colorOf(ColorRole.APP_BACKGROUND));
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(app.WIDTH, app.HEIGHT);
    }

    public class MinoCountPanel extends GameElementPanel {
        JLabel label;
        public MinoCountPanel() {
            super(TetrisView.this.app);
            label = new JLabel();
            label.setOpaque(false);
            label.setFont(new Font("Helvetica", Font.PLAIN, 18));
            add(label);
        }

        @Override
        public void paintComponent(Graphics g) {
            label.setForeground(app.colorOf(ColorRole.TEXT_COLOR));
            label.setText(""+(40 - model.getLinesCleared()));
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(84, 30);
        }
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
