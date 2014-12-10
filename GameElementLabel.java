import java.awt.Dimension;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class GameElementLabel extends GameElementPanel {

    private int HEIGHT = 28;
    private TetrisUILabel label;

    public GameElementLabel(AppController a, JComponent parent, String text) {
        super(a);
        label = new TetrisUILabel(app, text);
        add(label);
        int width = (int) parent.getPreferredSize().getWidth();
        setPreferredSize(new Dimension(width, HEIGHT));
    }
}
