package tetris.utilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class TetrisUILabel extends JLabel {

    public TetrisUILabel(String text) {
        super(text);
        this.setFont(new Font("Helvetica", Font.PLAIN, 14));
        this.setForeground(Color.black);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
