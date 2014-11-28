import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Tetris");

        TetrisModel model = new TetrisModel();
        TetrisView view = new TetrisView(model);
        TetrisController controller = new TetrisController(model, view);
        view.setController(controller);

        frame.add(view);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
