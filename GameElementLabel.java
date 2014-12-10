import java.awt.Dimension;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class GameElementLabel extends GameElementPanel {

    private int HEIGHT = 28;
    private TetrisUILabel label;

    // This is really just a helper class for adding label panels directly
    // above (and of the same width as) some GameElementPanel in the TetrisView
    public GameElementLabel(AppController a, JComponent parent, String text) {
        super(a);
        label = new TetrisUILabel(app, text);
        add(label);
        int width = (int) parent.getPreferredSize().getWidth();
        setPreferredSize(new Dimension(width, HEIGHT));
    }
}
