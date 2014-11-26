import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	public static final int INTERVAL = 30;
	public GamePanel() {
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start();
		setFocusable(true);
	}

	void tick() {
		System.out.println("Perform tick");
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
