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

    public static Color BG_COLOR = new Color(247,247,247);

    public final int WIDTH = 740;
    public final int HEIGHT= 540;

    private boolean hasSound;
    private boolean isDark;

    private JFrame mainFrame;
    private NavigationBarUpper upperNavigation;
    private NavigationBarLower lowerNavigation;
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

        hasSound= true;
        isDark  = true;

        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());

        cardsPanel = new JPanel();
        cardsLayout = new CardLayout();
        cardsPanel.setLayout(cardsLayout);

        home = new HomePanel(this);
        help = new HelpWindow(this);
        statsView = new GameStatisticsView(this);

        gameView  = new TetrisView(this);
        gameController = new TetrisController(this, gameView);
        initializeNewGame();

        scoresModel = new HighScoresModel();
        scoresView  = new HighScoresView(this, scoresModel);

        cardsPanel.add(home, "home");
        cardsPanel.add(gameView, "game");
        cardsPanel.add(scoresView, "scoresView");
        cardsPanel.add(statsView, "statsView");

        upperNavigation = new NavigationBarUpper(this);
        lowerNavigation = new NavigationBarLower(this);

        parent.add(upperNavigation, BorderLayout.NORTH);
        parent.add(cardsPanel, BorderLayout.CENTER);
        parent.add(lowerNavigation, BorderLayout.SOUTH);
        mainFrame.add(parent);
        cardsLayout.show(cardsPanel, "home");

        addFocusListener();
        home.requestFocus();
    }

    private void addFocusListener() {
        mainFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (gameModel != null) {
                    switch (gameModel.getStatus()) {
                        case PAUSED:
                            gameModel.setStatus(GameStatus.PLAYING);
                            break;
                        case COUNT_DOWN_PAUSED:
                            gameModel.setStatus(GameStatus.COUNT_DOWN);
                            break;
                        default:
                            break;
                    }
                }
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                if (gameModel != null) {
                    switch (gameModel.getStatus()) {
                        case PLAYING:
                            gameModel.setStatus(GameStatus.PAUSED);
                            break;
                        case COUNT_DOWN:
                            gameModel.setStatus(GameStatus.COUNT_DOWN_PAUSED);
                            break;
                        default:
                            break;
                    }
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
        upperNavigation.updateButtonStates(status);
        switch (status) {
            case AFTER_GAME:
                showGameStatistics();
                break;
            default:
                break;
        }
    }

    public void togglePlayPause() {
        playSound("/sounds/hold.wav");
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

    public void submitScore(String name, long time) {
        scoresModel.postScore(name, time);
        showHighScores();
    }

    public void showGameStatistics() {
        statsView.refreshData();
        cardsLayout.show(cardsPanel, "statsView");
    }

    public void showHome() {
        gameController.closeGame();
        initializeNewGame();
        upperNavigation.updateButtonStates(getStatus());
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

    public void playSound(String soundPath) {
        if (hasSound) {
            Sound.play(soundPath);
        }
    }

    public void toggleSound() {
        hasSound = !hasSound;
    }

    public void toggleLightDark() {
        isDark = !isDark;
        mainFrame.repaint();
    }

    public boolean hasSound() {
        return hasSound;
    }

    public boolean isDark() {
        return isDark;
    }

    public Color colorOf(ColorRole role) {
        if (isDark) {
            return ColorPalettes.getDarkPalette().get(role);
        } else {
            return ColorPalettes.getLightPalette().get(role);
        }
    }
}
