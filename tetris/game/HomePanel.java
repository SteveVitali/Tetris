package tetris.game;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tetris.utilities.TetrisUIButton;
import tetris.utilities.TetrisUIPanel;

@SuppressWarnings("serial")
public class HomePanel extends TetrisUIPanel {

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

        add(playButton);
    }

    public void playGame() {
        app.startNewGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(app.WIDTH, app.HEIGHT);
    }
}
