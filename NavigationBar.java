import java.awt.Color;

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

        playButton = new JButton("Play");
        highScoresButton = new JButton("High Scores");
        connectButton = new JButton("Connect");
        helpButton = new JButton("Help");

        add(playButton);
        add(highScoresButton);
        add(connectButton);
        add(helpButton);
    }
}
