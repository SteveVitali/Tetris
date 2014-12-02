import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class NavigationBar extends JMenuBar {

    private AppController app;
    private JButton playButton;
    private JButton highScoresButton;
    private JButton connectButton;
    private JButton helpButton;

    public NavigationBar(AppController app) {
        this.app = app;

        setBackground(Color.black);

        playButton = new JButton("Pause");
        highScoresButton = new JButton("High Scores");
        connectButton = new JButton("Connect");
        helpButton = new JButton("Help");

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
    }

    public void togglePlayPause() {
        app.togglePlayPause();
    }

    public void updatePlayPauseButton(GameStatus status) {
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
