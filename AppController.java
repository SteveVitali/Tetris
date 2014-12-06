import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppController {

    public static Color BG_COLOR = new Color(26,26,26);

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

    private GameStatisticsView statsView;

    public AppController(JFrame mainFrame) {
        this.setMainFrame(mainFrame);

        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());

        cardsPanel = new JPanel();
        cardsLayout = new CardLayout();
        cardsPanel.setLayout(cardsLayout);

        home = new HomePanel(this);
        help = new HelpWindow();
        statsView = new GameStatisticsView(this);

        gameView  = new TetrisView(this);
        gameController = new TetrisController(gameView);
        initializeNewGame();

        scoresModel = new HighScoresModel();
        scoresView  = new HighScoresView(scoresModel);

        cardsPanel.add(home, "home");
        cardsPanel.add(gameView, "game");
        cardsPanel.add(scoresView, "scoresView");
        cardsPanel.add(statsView, "statsView");

        navigation = new NavigationBar(this);

        parent.add(navigation, BorderLayout.NORTH);
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

    public void initializeNewGame() {
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
        statsView.setModel(gameModel);
    }

    public void startNewGame() {
        initializeNewGame();
        cardsLayout.show(cardsPanel, "game");
        gameView.requestFocus();
        gameController.startGame();
    }

    private void gameStatusChanged(GameStatus status) {
        navigation.updateButtonStates(status);
        switch (status) {
            case AFTER_GAME:
                showGameStatistics();
                break;
            default:
                break;
        }
    }

    public void togglePlayPause() {
        gameController.togglePlayPause();
        gameView.requestFocus();
    }

    public void showHelp() {
        help.setVisible(true);
        if (gameModel.getStatus() == GameStatus.PLAYING) {
            gameModel.setStatus(GameStatus.PAUSED);
        }
    }

    public void showHighScores() {
        cardsLayout.show(cardsPanel, "scoresView");
    }

    public void showGameStatistics() {
        cardsLayout.show(cardsPanel, "statsView");
    }

    public void returnToHome() {
        gameController.closeGame();
        initializeNewGame();
        navigation.updateButtonStates(getStatus());
        cardsLayout.show(cardsPanel, "home");
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
