
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
    private AppController app;

    public HelpWindow(AppController a) {
        this.app = a;

        jfxPanel.setPreferredSize(new Dimension(600,600));
        setSize(jfxPanel.getPreferredSize());

        // Load the help HTML in the JFXPanel
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView();
                webEngine = view.getEngine();
                jfxPanel.setScene(new Scene(view));
                String instructions = app.isDark()
                        ? "/help/index-dark.html"
                        : "/help/index-light.html";
                URL htmlURL = getClass().getResource(instructions);
                webEngine.load(htmlURL.toExternalForm());
            }
        });

        setVisible(false);
        setTitle("Help");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        add(jfxPanel);
        pack();
    }
}
