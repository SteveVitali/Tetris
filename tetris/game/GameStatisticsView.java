package tetris.game;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameStatisticsView extends JPanel {

    TetrisModel model;
    AppController app;

    public GameStatisticsView(AppController a) {
        this.app = a;
        setBackground(AppController.BG_COLOR);

        JLabel time = new JLabel();
    }

    public void setModel(TetrisModel m) {
        this.model = m;
    }
}
