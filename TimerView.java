import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class TimerView extends GameElementPanel {

    private int WIDTH  = 84;
    private int HEIGHT = 38;
    private TimerModel model;

    public TimerView(TimerModel m) {
        this.model = m;
        this.setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 18));
        g.drawString(model.getTimeString(), 8, 24);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
