import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameElementPanel extends JPanel {

    protected AppController app;
    // GameElementPanels are used for the mino panels, the timer panel,
    // the panel that displays lines remaining, and all GameElementLabel's
    // in the TetrisView.
    public GameElementPanel(AppController a) {
        this.app = a;
        setBackground(null);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(app.colorOf(ColorRole.DARK_ACCENT));
        g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
    }
}
