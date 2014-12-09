
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {

    private AppController app;
    private TetrisUIButton playButton;

    public HomePanel(AppController app) {
        this.app = app;

        playButton = new TetrisUIButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playGame();
            }
        });
        playButton.setFocusable(false);
        add(playButton);
    }

    public void playGame() {
        app.startNewGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        setBackground(app.colorOf(ColorRole.APP_BACKGROUND));
        super.paintComponent(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(app.WIDTH, app.HEIGHT);
    }
}
