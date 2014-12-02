import java.awt.BorderLayout;
import java.awt.CardLayout;
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

    public AppController(JFrame mainFrame) {
        this.mainFrame = mainFrame;

        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());

        navigation = new NavigationBar(this);
        parent.add(navigation, BorderLayout.NORTH);

        cardsPanel = new JPanel();
        cardsLayout = new CardLayout();
        cardsPanel.setLayout(cardsLayout);

        home = new HomePanel(this);

        model = new TetrisModel();
        game  = new TetrisView(this, model);
        controller = new TetrisController(model, game);
        game.setController(controller);

        cardsPanel.add(home, "home");
        cardsPanel.add(game, "game");

        parent.add(cardsPanel, BorderLayout.CENTER);
        mainFrame.add(parent);
        cardsLayout.show(cardsPanel, "home");
    }

    public void playGame() {
        cardsLayout.show(cardsPanel, "game");
        game.requestFocus();
    }
}
