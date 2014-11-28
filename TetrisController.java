import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class TetrisController {

    private TetrisModel model;
    private TetrisView view;

    public static final int TICK_INTERVAL = 30;
    public static final int DROP_INTERVAL = 1200;

    public TetrisController(TetrisModel m, TetrisView v) {
        this.model = m;
        this.view  = v;

        Timer tickTimer = new Timer(TICK_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        tickTimer.start();

        Timer dropTimer = new Timer(DROP_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                softDropCurrentMino();
            }
        });
        dropTimer.start();
    }

    private void tick() {
        Action action;
        while ((action = model.popAction()) != null) {
            switch (action) {
            case MOVE_LEFT:
                model.tryMove(-1,0);
                break;
            case MOVE_RIGHT:
                model.tryMove(1,0);
                break;
            case HOLD:
                model.hold();
                break;
            case HARD_DROP:
                model.hardDrop();
                break;
            case SOFT_DROP:
                model.tryMove(0,-1);
                break;
            case ROTATE_CLOCKWISE:
                model.rotateClockwise();
                break;
            case ROTATE_COUNTER_CLOCKWISE:
                model.rotateCounterClockwise();
                break;
            }
        }
        view.repaint();
    }

    private void softDropCurrentMino() {
        model.addAction(Action.SOFT_DROP);
    }
}
