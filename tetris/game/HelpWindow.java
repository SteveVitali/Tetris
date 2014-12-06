package tetris.game;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFrame;
import java.net.URL;

@SuppressWarnings("serial")
public class HelpWindow extends JFrame {

    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine webEngine;

    public HelpWindow() {

        jfxPanel.setPreferredSize(new Dimension(600,600));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView();
                webEngine = view.getEngine();
                jfxPanel.setScene(new Scene(view));
                URL htmlURL = getClass().getResource("/help/index.html");
                webEngine.load(htmlURL.toExternalForm());
            }
        });

        setVisible(false);
        setTitle("Help");
        setSize(jfxPanel.getPreferredSize());
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        add(jfxPanel);
        pack();
    }
}
