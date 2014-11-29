import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GameElementPanel extends JPanel {

    public GameElementPanel() {
        setBackground(null);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(100, 100, 100, 128));
        g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
    }
}
