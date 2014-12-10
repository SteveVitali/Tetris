import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
// Useful class for enforcing consistent button styling across the app
public class TetrisUIButton extends JButton {

    public TetrisUIButton() {
        this.setFont(new Font("Helvetica", Font.PLAIN, 14));
        setFocusable(false);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.BOTTOM);
    }

    public TetrisUIButton(String text) {
        super(text);
    }

    public TetrisUIButton(String text, String imgPath) {
        this(text);
        setIcon(imgPath);
    }

    public void setIcon(String imgPath) {
        try {
            Image img = ImageIO.read(getClass().getResource(imgPath));
            setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
}
