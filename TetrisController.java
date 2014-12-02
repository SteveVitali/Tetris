import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class TetrisController {

    private TetrisModel model;
    private TetrisView view;
    @SuppressWarnings("unused")
    private AppController app;

    private Timer tickTimer;

    public static final int DROP_INTERVAL = 900;
    public static final int TICK_INTERVAL = 30;

    public TetrisController(AppController a, TetrisModel m, TetrisView v) {
        this.app   = a;
        this.model = m;
        this.view  = v;

        initGame();
    }

    public void initGame() {
        model.setTime(0);
        tickTimer = new Timer(TICK_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
                if (model.getStatus() == GameStatus.PLAYING) {
                    model.addTime(TICK_INTERVAL);
                    if (model.getTimer().getTime() % DROP_INTERVAL == 0) {
                        softDropCurrentMino();
                    }
                }
            }
        });
        tickTimer.start();
    }

    public void togglePlayPause() {
        switch (model.getStatus()) {
            case PLAYING:
                model.setStatus(GameStatus.PAUSED);
                break;
            case PAUSED:
                model.setStatus(GameStatus.PLAYING);
                break;
            default:
                break;
        }
    }

    private void tick() {
        Action action;
        while ((action = model.popAction()) != null) {
            switch (action) {
            case MOVE_LEFT:
                model.tryMoveX(-1);
                break;
            case MOVE_RIGHT:
                model.tryMoveX(1);
                break;
            case HOLD:
                model.hold();
                break;
            case HARD_DROP:
                model.hardDrop();
                break;
            case SOFT_DROP:
                model.tryMoveY(1);
                break;
            case ROTATE_CLOCKWISE:
                model.tryRotateClockwise();
                break;
            case ROTATE_COUNTER_CLOCKWISE:
                model.tryRotateCounterClockwise();
                break;
            case LOCK_MINO:
                model.lockMinoIntoMatrix();
                break;
            case TOGGLE_PAUSE:
                togglePlayPause();
                break;
            }
            model.checkHitBottom();
        }
        view.repaint();
    }

    private void softDropCurrentMino() {
        model.addAction(Action.SOFT_DROP);
    }
}
