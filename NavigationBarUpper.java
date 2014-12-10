
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class NavigationBarUpper extends JMenuBar {

    private AppController app;
    private TetrisUIButton playButton;
    private TetrisUIButton highScoresButton;
    private TetrisUIButton connectButton;
    private TetrisUIButton helpButton;
    private TetrisUIButton abortButton;

    private String PLAY_PATH = "/images/flat-play.png";
    private String PAUSE_PATH= "/images/flat-pause.png";
    private String STOP_PATH = "/images/flat-stop.png";
    private String HELP_PATH = "/images/flat-help.png";
    private String TROPHY_PATH = "/images/flat-trophy.png";

    public NavigationBarUpper(AppController app) {
        this.app = app;

        setBackground(Color.black);
        setLayout(new BorderLayout());

        JPanel west = new NavPanel();
        JPanel center = new NavPanel();
        JPanel east = new NavPanel();

        addPlayButton(west);
        addAbortButton(west);
        addHighScoresButton(center);
        addConnectButton(center);
        addHelpButton(east);

        add(west, BorderLayout.WEST);
        add(east, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        updateButtonStates(app.getStatus());
    }

    private void addPlayButton(JPanel panel) {
        playButton = new TetrisUIButton();
        playButton.setIcon(PLAY_PATH);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playPauseButtonClicked();
                app.playSound("/sounds/hold.wav");
            }
        });

        panel.add(playButton);
    }

    private void addAbortButton(JPanel panel) {
        abortButton = new TetrisUIButton();
        abortButton.setIcon(STOP_PATH);
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHome();
                app.playSound("/sounds/hold.wav");
            }
        });
        panel.add(abortButton);
    }

    private void addHighScoresButton(JPanel panel) {
        highScoresButton = new TetrisUIButton();
        highScoresButton.setIcon(TROPHY_PATH);
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHighScores();
                app.playSound("/sounds/hold.wav");
            }
        });
        panel.add(highScoresButton);
    }

    private void addConnectButton(JPanel panel) {
        connectButton = new TetrisUIButton("Connect");
        //panel.add(connectButton);
    }

    private void addHelpButton(JPanel panel) {
        helpButton = new TetrisUIButton();
        helpButton.setIcon(HELP_PATH);

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showHelp();
                app.playSound("/sounds/hold.wav");
            }
        });
        panel.add(helpButton);
    }

    public void playPauseButtonClicked() {
        switch (app.getStatus()) {
            case AFTER_GAME:
            case BEFORE_GAME:
                app.startNewGame();
                break;
            case PLAYING:
            case PAUSED:
            case COUNT_DOWN:
            case COUNT_DOWN_PAUSED:
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
            case COUNT_DOWN:
                playButton.setIcon(PAUSE_PATH);
                highScoresButton.setEnabled(false);
                connectButton.setEnabled(false);
                abortButton.setVisible(true);
                break;
            case PAUSED:
            case COUNT_DOWN_PAUSED:
                playButton.setIcon(PLAY_PATH);
                highScoresButton.setEnabled(false);
                connectButton.setEnabled(false);
                abortButton.setVisible(true);
                break;
            case AFTER_GAME:
            case BEFORE_GAME:
                playButton.setIcon(PLAY_PATH);
                highScoresButton.setEnabled(true);
                connectButton.setEnabled(true);
                abortButton.setVisible(false);
            default:
                break;
        }
    }

    private class NavPanel extends JPanel {
        public NavPanel() {
            setOpaque(false);
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(110, 60);
        }
    }
}
