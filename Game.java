import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Tetris");

        new AppController(frame);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
