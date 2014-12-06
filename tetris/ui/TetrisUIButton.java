package tetris.ui;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class TetrisUIButton extends JButton {

    public TetrisUIButton(String text) {
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
