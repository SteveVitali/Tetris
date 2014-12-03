import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppController {

    public final int WIDTH = 800;
    public final int HEIGHT= 512;

    private JFrame mainFrame;
    private NavigationBar navigation;
    private CardLayout cardsLayout;
    private JPanel cardsPanel;
    private HomePanel home;
    private TetrisView game;
    private TetrisModel model;
    private TetrisController controller;
    private HelpWindow help;

    public AppController(JFrame mainFrame) {
        this.setMainFrame(mainFrame);

        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());

        navigation = new NavigationBar(this);
        parent.add(navigation, BorderLayout.NORTH);

        help = new HelpWindow();

        cardsPanel = new JPanel();
        cardsLayout = new CardLayout();
        cardsPanel.setLayout(cardsLayout);

        home = new HomePanel(this);

        model = new TetrisModel();
        game  = new TetrisView(this, model);
        controller = new TetrisController(this, model, game);
        game.setController(controller);

        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("status")) {
                    GameStatus newStatus = (GameStatus)e.getNewValue();
                    navigation.updatePlayPauseButton(newStatus);
                }
            }
        });

        cardsPanel.add(home, "home");
        cardsPanel.add(game, "game");

        parent.add(cardsPanel, BorderLayout.CENTER);
        mainFrame.add(parent);
        cardsLayout.show(cardsPanel, "home");
        home.requestFocus();
    }

    public void playGame() {
        navigation.togglePlayPause();
        cardsLayout.show(cardsPanel, "game");
        game.requestFocus();
    }

    public void togglePlayPause() {
        game.requestFocus();
        controller.togglePlayPause();
    }

    public void showHelp() {
        help.setVisible(true);
        model.setStatus(GameStatus.PAUSED);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
