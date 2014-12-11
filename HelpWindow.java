import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

@SuppressWarnings("serial")
public class HelpWindow extends JFrame {

    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine webEngine;
    private boolean USE_JFX = true;

    public HelpWindow() {
        setPreferredSize(new Dimension(600,600));
        setTitle("Help");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        if (USE_JFX) {
            initUsingJFX();
        }
        else {
            initUsingSwing();
        }
        setVisible(false);
        pack();
    }

    private void initUsingJFX() {
        jfxPanel.setPreferredSize(new Dimension(600,600));
        setSize(jfxPanel.getPreferredSize());

        // Load the help HTML in the JFXPanel
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView();
                webEngine = view.getEngine();
                jfxPanel.setScene(new Scene(view));
                String instructions = "/help/index-jfx.html";
                URL htmlURL = getClass().getResource(instructions);
                webEngine.load(htmlURL.toExternalForm());
            }
        });
        add(jfxPanel);
    }

    private void initUsingSwing() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JEditorPane jEditorPane = new JEditorPane();
                jEditorPane.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(jEditorPane);

                HTMLEditorKit kit = new HTMLEditorKit();
                jEditorPane.setEditorKit(kit);

                StringBuilder contentBuilder = new StringBuilder();
                try {
                    URL fileURL = this.getClass().getResource("help/index-no-jfx.html");
                    BufferedReader in = new BufferedReader(new FileReader(fileURL.getPath()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        contentBuilder.append(str);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String html = contentBuilder.toString();

                Document doc = kit.createDefaultDocument();
                jEditorPane.setDocument(doc);
                jEditorPane.setText(html);

                getContentPane().add(scrollPane, BorderLayout.CENTER);
            }
        });
    }
}
