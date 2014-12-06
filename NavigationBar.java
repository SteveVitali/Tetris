import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class NavigationBar extends JMenuBar {

    private AppController app;
    private TetrisButton playButton;
    private TetrisButton highScoresButton;
    private TetrisButton connectButton;
    private TetrisButton helpButton;
    private TetrisButton abortButton;

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
        playButton = new TetrisButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPauseButtonClicked();
            }
        });

        add(playButton);
    }

    private void addAbortButton() {
        abortButton = new TetrisButton("Abort");
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.returnToHome();
            }
        });
        add(abortButton);
    }

    private void addHighScoresButton() {
        highScoresButton = new TetrisButton("High Scores");
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHighScores();
            }
        });
        add(highScoresButton);
    }

    private void addConnectButton() {
        connectButton = new TetrisButton("Connect");
        add(connectButton);
    }

    private void addHelpButton() {
        helpButton = new TetrisButton("Help");

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
