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

    private TetrisView gameView;
    private TetrisModel gameModel;
    private TetrisController gameController;

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

        gameView  = new TetrisView(this);
        gameController = new TetrisController(gameView);

        scoresModel = new HighScoresModel();
        scoresView  = new HighScoresView(scoresModel);

        cardsPanel.add(home, "home");
        cardsPanel.add(gameView, "game");
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
                if (gameModel != null &&
                    gameModel.getStatus() == GameStatus.PAUSED) {
                    gameModel.setStatus(GameStatus.PLAYING);
                }
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                if (gameModel != null &&
                    gameModel.getStatus() == GameStatus.PLAYING) {
                    gameModel.setStatus(GameStatus.PAUSED);
                }
            }
        });
    }

    private void gameStatusChanged(GameStatus status) {
        navigation.updateButtonStates(status);
        switch (status) {
            case GAME_OVER:
                String text = JOptionPane.showInputDialog("GAME OVER \nName:");
                scoresModel.addScore(text, gameModel.getFinalTime());
                cardsLayout.show(cardsPanel, "scoresView");
                break;
            default:
                break;
        }
    }

    public void startNewGame() {
        gameModel = new TetrisModel();
        gameModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("status")) {
                    GameStatus newStatus = (GameStatus)e.getNewValue();
                    gameStatusChanged(newStatus);
                }
            }
        });
        gameController.setModel(gameModel);
        cardsLayout.show(cardsPanel, "game");
        gameView.requestFocus();
        gameController.startGame();
    }

    public void togglePlayPause() {
        gameController.togglePlayPause();
        gameView.requestFocus();
    }

    public void showHelp() {
        help.setVisible(true);
        gameModel.setStatus(GameStatus.PAUSED);
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public GameStatus getStatus() {
        return gameModel.getStatus();
    }
}
