import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
// A TimerView displays the data of a TimerModel
public class TimerView extends GameElementPanel {

    private int WIDTH  = 84;
    private int HEIGHT = 38;
    private TimerModel model;

    public TimerView(AppController a) {
        super(a);
    }

    public TimerView(AppController a, TimerModel m) {
        super(a);
        setModel(m);
        this.setOpaque(false);
    }

    public void setModel(TimerModel m) {
        this.model = m;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(app.colorOf(ColorRole.TEXT_COLOR));
        g2d.setFont(new Font("Helvetica", Font.PLAIN, 18));
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP
        );
        g2d.drawString(model.getTimeString(), 8, 24);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
