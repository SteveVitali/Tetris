package tetris.game;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;

import tetris.utilities.TetrisUIButton;

@SuppressWarnings("serial")
public class NavigationBar extends JMenuBar {

    private AppController app;
    private TetrisUIButton playButton;
    private TetrisUIButton highScoresButton;
    private TetrisUIButton connectButton;
    private TetrisUIButton helpButton;
    private TetrisUIButton abortButton;

    public NavigationBar(AppController app) {
        this.app = app;

        setBackground(Color.black);

        addPlayButton();
        addAbortButton();
        addHighScoresButton();
        addConnectButton();
        addHelpButton();

        updateButtonStates(app.getStatus());
    }

    private void addPlayButton() {
        playButton = new TetrisUIButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPauseButtonClicked();
            }
        });

        add(playButton);
    }

    private void addAbortButton() {
        abortButton = new TetrisUIButton("Abort");
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.returnToHome();
            }
        });
        add(abortButton);
    }

    private void addHighScoresButton() {
        highScoresButton = new TetrisUIButton("High Scores");
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHighScores();
            }
        });
        add(highScoresButton);
    }

    private void addConnectButton() {
        connectButton = new TetrisUIButton("Connect");
        add(connectButton);
    }

    private void addHelpButton() {
        helpButton = new TetrisUIButton("Help");

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHelp();
            }
        });

        add(helpButton);
    }

    public void playPauseButtonClicked() {
        switch (app.getStatus()) {
            case AFTER_GAME:
            case BEFORE_GAME:
                app.startNewGame();
                break;
            case PLAYING:
            case PAUSED:
                app.togglePlayPause();
                break;
            default:
                break;
        }
    }

    public void updateButtonStates(GameStatus status) {
        playButton.setEnabled(true);
        switch (status) {
            case PLAYING:
                playButton.setText("Pause");
                highScoresButton.setEnabled(false);
                connectButton.setEnabled(false);
                abortButton.setVisible(true);
                break;
            case PAUSED:
                playButton.setText("Play");
                highScoresButton.setEnabled(false);
                connectButton.setEnabled(false);
                abortButton.setVisible(true);
                break;
            case AFTER_GAME:
            case BEFORE_GAME:
                playButton.setText("Play");
                highScoresButton.setEnabled(true);
                connectButton.setEnabled(true);
                abortButton.setVisible(false);
            default:
                break;
        }
    }
}
