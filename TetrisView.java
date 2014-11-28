import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TetrisView extends JPanel {

    private int WIDTH = 512;
    private int HEIGHT= 412;

    private TetrisModel model;
    private TetrisController controller;
    private MatrixView matrix;

    public TetrisView(TetrisModel m) {
        this.model = m;
        this.matrix = new MatrixView(model.getMatrix());
        add(matrix, BorderLayout.CENTER);
        setLayout( new FlowLayout() );
        addKeyListener(new KeyHandler(model));
        setFocusable(true);
        setBackground(Color.black);
    }

    public void setController(TetrisController c) {
        this.controller = c;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
