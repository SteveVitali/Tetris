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

        playButton = new TetrisButton("Pause");
        highScoresButton = new TetrisButton("High Scores");
        connectButton = new TetrisButton("Connect");
        helpButton = new TetrisButton("Help");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePlayPause();
            }
        });

        add(playButton);
        add(highScoresButton);
        add(connectButton);
        add(helpButton);

        playButton.setEnabled(false);
    }

    public void togglePlayPause() {
        app.togglePlayPause();
    }

    public void updatePlayPauseButton(GameStatus status) {
        playButton.setEnabled(true);
        switch (status) {
            case PLAYING:
                playButton.setText("Pause");
                break;
            case PAUSED:
                playButton.setText("Play");
                break;
            default:
                break;
        }
    }
}
