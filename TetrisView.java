import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
// A TetrisView draws the entire tetris game, including all the
// HUD, the game matrix, the next/hold queue, et cetera
public class TetrisView extends JPanel {

    private AppController app;
    private TetrisModel model;
    @SuppressWarnings("unused")
    private TetrisController controller;
    private MatrixView matrix;
    private QueueView queue;
    private TimerView timer;
    private MinoPanel hold;
    private LineCountPanel lineCount;

    public TetrisView(AppController app) {
        this.app = app;

        this.matrix = new MatrixView(app);
        this.queue  = new QueueView (app);
        this.timer  = new TimerView (app);
        this.hold   = new MinoPanel(app);

        this.lineCount = new LineCountPanel();

        GameElementLabel timerLabel = new GameElementLabel(app, timer, "Time");
        GameElementLabel holdLabel  = new GameElementLabel(app, hold, "Hold");
        GameElementLabel nextLabel  = new GameElementLabel(app, hold, "Next");
        GameElementPanel linesLabel = new GameElementLabel(app, lineCount, "Lines");

        JPanel west = new JPanel();
        west.setPreferredSize(new Dimension(100, 512));
        west.setOpaque(false);
        west.add(holdLabel);
        west.add(hold);
        west.add(new SpacerPanel(MinoPanel.WIDTH, 12));
        west.add(timerLabel);
        west.add(timer);
        west.add(new SpacerPanel(MinoPanel.WIDTH, 12));
        west.add(linesLabel);
        west.add(lineCount);

        JPanel east = new JPanel();
        east.setPreferredSize(new Dimension(100, 512));
        east.setOpaque(false);
        east.add(nextLabel);
        east.add(queue);

        add(west  , BorderLayout.WEST);
        add(matrix, BorderLayout.CENTER);
        add(east , BorderLayout.EAST);

        addKeyListener(new KeyHandler());
        setFocusable(true);
    }

    // When we set a new model on a TetrisView, we need to also
    // update the references to matrix/queue/timer models, and we need
    // to initialize our property change listeners on the model
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
                // When the next queue changes, redraw the queue
                if (e.getPropertyName().equals("queue")) {
                    queue.updateMinoPanels();
                }
            }
        });

        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                // If the holdMino has changed, draw the change in the
                // hold MinoPanel
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
    public void paint(Graphics g) {
       super.paint(g);
       // We override paint so the countdown appears above the matrix view
       switch (model.getStatus()) {
           case COUNT_DOWN:
           case COUNT_DOWN_PAUSED:
               int time = model.getCountDownTimeInt();
               Graphics2D g2d = (Graphics2D) g;
               g2d.setColor(app.colorOf(ColorRole.TEXT_COLOR));
               g2d.setFont(new Font("Helvetica", Font.PLAIN, 24));
               g2d.setRenderingHint(
                       RenderingHints.KEY_TEXT_ANTIALIASING,
                       RenderingHints.VALUE_TEXT_ANTIALIAS_GASP
               );
               g2d.drawString(""+time, getWidth()/2-6, getHeight()/3);
               break;
           default:
               break;
       }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(app.WIDTH, app.HEIGHT);
    }

    // We use this for our panel that displays the line count
    private class LineCountPanel extends GameElementPanel {
        JLabel label;
        public LineCountPanel() {
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

    // Constructs a transparent spacer of given width and height
    private class SpacerPanel extends JPanel {
        public SpacerPanel(int width, int height) {
            setOpaque(false);
            setPreferredSize(new Dimension(width, height));
        }
    }

    // On key events, we delegate to the model's setKeyPressed method
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
