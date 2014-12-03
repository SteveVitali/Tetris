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

    public NavigationBar(AppController app) {
        this.app = app;

        setBackground(Color.black);

        addPlayButton();
        addHighScoresButton();
        addConnectButton();
        addHelpButton();

        playButton.setEnabled(false);
    }

    private void addPlayButton() {
        playButton = new TetrisButton("Pause");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPauseButtonClicked();
            }
        });

        add(playButton);
    }

    private void addHighScoresButton() {
        highScoresButton = new TetrisButton("High Scores");
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
            case GAME_OVER:
            case BEFORE_GAME:
                app.startNewGame();
                break;
            case PLAYING:
            case PAUSED:
                app.togglePlayPause();
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
                break;
            case PAUSED:
                playButton.setText("Play");
                highScoresButton.setEnabled(false);
                connectButton.setEnabled(false);
                break;
            case GAME_OVER:
            case BEFORE_GAME:
                playButton.setText("Play");
                highScoresButton.setEnabled(true);
                connectButton.setEnabled(true);
            default:
                break;
        }
    }
}
