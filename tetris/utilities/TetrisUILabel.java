package tetris.utilities;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import tetris.game.AppController;

@SuppressWarnings("serial")
public class TetrisUILabel extends JLabel {

    private AppController app;
    public TetrisUILabel(AppController a, String text) {
        super(text);
        this.app = a;
        this.setFont(new Font("Helvetica", Font.PLAIN, 14));
    }

    @Override
    public void paintComponent(Graphics g) {
        setForeground(app.colorOf(ColorRole.TEXT_COLOR));
        super.paintComponent(g);
    }
}
