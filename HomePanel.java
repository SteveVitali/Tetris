import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {

    private AppController app;
    private JButton playButton;

    public HomePanel(AppController app) {
        this.app = app;
        setBackground(Color.black);

        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playGame();
            }
        });

        add(playButton);
    }

    public void playGame() {
        app.playGame();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(app.WIDTH, app.HEIGHT);
    }
}
