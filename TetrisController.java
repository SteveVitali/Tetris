
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class TetrisController {

    private AppController app;
    private TetrisModel model;
    private TetrisView view;

    private Timer tickTimer;

    public static final int DROP_INTERVAL = 900;
    public static final int TICK_INTERVAL = 30;

    public TetrisController(AppController a, TetrisView v) {
        this.app = a;
        setView(v);
    }

    public void setView(TetrisView v) {
        this.view = v;
    }

    public void setModel(TetrisModel m) {
        this.model = m;
        this.view.setModel(m);
    }

    public void startGame() {
        model.setCountDownTime(model.COUNTDOWN_LENGTH);
        model.setStatus(GameStatus.COUNT_DOWN);
        model.setTime(0);
        app.playSound("/sounds/count_down.wav");

        tickTimer = new Timer(TICK_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        tickTimer.start();
    }

    public void closeGame() {
        tickTimer.stop();
    }

    public void endGame() {
        closeGame();
        model.setStatus(GameStatus.AFTER_GAME);
    }

    private void tick() {
        doActions();

        switch (model.getStatus()) {
            case PLAYING:
                model.addTime(TICK_INTERVAL);
                if (model.getTimer().getTime() % DROP_INTERVAL == 0) {
                    softDropCurrentMino();
                }
                break;
            case COUNT_DOWN:
                int oldTimeInt = model.getCountDownTimeInt();
                long newTime   = model.getCountDownTime() - TICK_INTERVAL;
                model.setCountDownTime(newTime);
                if (oldTimeInt != model.getCountDownTimeInt()) {
                    app.playSound("/sounds/count_down.wav");
                }
                if (newTime <= 0) {
                    model.setStatus(GameStatus.PLAYING);
                }
                break;
            case GAME_OVER:
                endGame();
                break;
            case PAUSED:
                break;
            case COUNT_DOWN_PAUSED:
                break;
            case BEFORE_GAME:
                break;
            case AFTER_GAME:
                break;
        }
        view.repaint();
    }

    private void doActions() {
        Action action;
        boolean hitWall = false;
        boolean rotated = false;
        boolean moved   = false;
        while ((action = model.popAction()) != null) {
            switch (action) {
            case MOVE_LEFT:
                moved = model.tryMoveX(-1);
                hitWall = moved ? false : true;
                break;
            case MOVE_RIGHT:
                moved = model.tryMoveX(1);
                hitWall = moved ? false : true;
                break;
            case HOLD:
                model.hold();
                app.playSound("/sounds/hold.wav");
                break;
            case HARD_DROP:
                model.hardDrop();
                app.playSound("/sounds/hard_drop.wav");
                break;
            case SOFT_DROP:
                model.tryMoveY(1);
                break;
            case ROTATE_CLOCKWISE:
                rotated = model.tryRotateClockwise();
                break;
            case ROTATE_COUNTER_CLOCKWISE:
                rotated = model.tryRotateCounterClockwise();
                break;
            case LOCK_MINO:
                model.lockMinoIntoMatrix();
                break;
            case TOGGLE_PAUSE:
                togglePlayPause();
                break;
            }
            model.checkHitBottom();
            if (rotated) {
                app.playSound("/sounds/rotate_whoosh.wav");
            }
            if (moved) {
                model.setHasHitWall(false);
                //app.playSound("/sounds/move-swish.wav");
            }
            if (hitWall && !model.getHasHitWall()) {
                model.setHasHitWall(true);
                app.playSound("/sounds/hit_wall.wav");
            }
        }
    }

    private void softDropCurrentMino() {
        model.addAction(Action.SOFT_DROP);
    }

    public void togglePlayPause() {
        System.out.println("status:"+model.getStatus());
        switch (model.getStatus()) {
            case COUNT_DOWN:
                model.setStatus(GameStatus.COUNT_DOWN_PAUSED);
                break;
            case COUNT_DOWN_PAUSED:
                model.setStatus(GameStatus.COUNT_DOWN);
                break;
            case PLAYING:
                model.setStatus(GameStatus.PAUSED);
                break;
            case PAUSED:
                model.setStatus(GameStatus.PLAYING);
                break;
            case BEFORE_GAME:
                model.setStatus(GameStatus.PLAYING);
                break;
            default:
                break;
        }
    }
}
