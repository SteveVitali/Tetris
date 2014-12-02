import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class TetrisButton extends JButton {

    public TetrisButton(String text) {
        super(text);
        this.setFont(new Font("Helvetica", Font.PLAIN, 14));
        setFocusable(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 30);
    }
}
