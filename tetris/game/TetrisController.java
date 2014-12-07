package tetris.game;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import tetris.utilities.Sound;

public class TetrisController {

    private TetrisModel model;
    private TetrisView view;

    private Timer tickTimer;

    public static final int DROP_INTERVAL = 900;
    public static final int TICK_INTERVAL = 30;

    public TetrisController(TetrisView v) {
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
        model.setStatus(GameStatus.PLAYING);
        model.setTime(0);

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
            case GAME_OVER:
                endGame();
                break;
            case PAUSED:
                break;
            case BEFORE_GAME:
                break;
            case AFTER_GAME:
                break;
        }
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
                Sound.play("/sounds/hold.wav");
                break;
            case HARD_DROP:
                model.hardDrop();
                Sound.play("/sounds/hard_drop.wav");
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
                Sound.play("/sounds/rotate_whoosh.wav");
            }
            if (moved) {
                model.setHasHitWall(false);
                //Sound.play("/sounds/move-swish.wav");
            }
            if (hitWall && !model.getHasHitWall()) {
                model.setHasHitWall(true);
                Sound.play("/sounds/hit_wall.wav");
            }
        }
        view.repaint();
    }

    private void softDropCurrentMino() {
        model.addAction(Action.SOFT_DROP);
    }

    public void togglePlayPause() {
        Sound.play("/sounds/tick.wav");
        switch (model.getStatus()) {
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
