import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TetrisMain implements Runnable {
	public void run() {
		final JFrame frame = new JFrame("Tetris");

		final GamePanel game = new GamePanel();
		frame.add(game, BorderLayout.CENTER);

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new TetrisMain());
	}
}
