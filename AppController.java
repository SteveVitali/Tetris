import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AppController {

    public final int WIDTH = 800;
    public final int HEIGHT= 512;

    private JFrame mainFrame;
    private NavigationBar navigation;
    private CardLayout cardsLayout;
    private JPanel cardsPanel;
    private HomePanel home;
    private HelpWindow help;

    private TetrisView game;
    private TetrisModel model;
    private TetrisController controller;

    private HighScoresModel scoresModel;
    private HighScoresView scoresView;

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
        controller = new TetrisController(model, game);
        game.setController(controller);

        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("status")) {
                    GameStatus newStatus = (GameStatus)e.getNewValue();
                    gameStatusChanged(newStatus);
                }
            }
        });

        scoresModel = new HighScoresModel();
        scoresView  = new HighScoresView(scoresModel);

        cardsPanel.add(home, "home");
        cardsPanel.add(game, "game");
        cardsPanel.add(scoresView, "scoresView");

        parent.add(cardsPanel, BorderLayout.CENTER);
        mainFrame.add(parent);
        cardsLayout.show(cardsPanel, "home");

        addFocusListener();
        home.requestFocus();
    }

    private void addFocusListener() {
        mainFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (model.getStatus() == GameStatus.PAUSED) {
                    model.setStatus(GameStatus.PLAYING);
                }
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                if (model.getStatus() == GameStatus.PLAYING) {
                    model.setStatus(GameStatus.PAUSED);
                }
            }
        });
    }

    private void gameStatusChanged(GameStatus status) {
        navigation.updateButtonStates(status);
        switch (status) {
            case GAME_OVER:
                String text = JOptionPane.showInputDialog("GAME OVER \nName:");
                scoresModel.addScore(text, model.getFinalTime());
                cardsLayout.show(cardsPanel, "scoresView");
                break;
            default:
                break;
        }
    }

    public void startNewGame() {
        cardsLayout.show(cardsPanel, "game");
        game.requestFocus();
        model.setStatus(GameStatus.PLAYING);
    }

    public void togglePlayPause() {
        controller.togglePlayPause();
        game.requestFocus();
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

    public GameStatus getStatus() {
        return model.getStatus();
    }
}
