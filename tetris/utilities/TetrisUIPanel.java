package tetris.utilities;
import javax.swing.JPanel;
import tetris.game.AppController;

@SuppressWarnings("serial")
public class TetrisUIPanel extends JPanel {

    public TetrisUIPanel() {
        setBackground(AppController.BG_COLOR);
    }
}
