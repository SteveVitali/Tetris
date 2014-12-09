package tetris.game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuBar;

import tetris.utilities.TetrisUIButton;

@SuppressWarnings("serial")
public class NavigationBarLower extends JMenuBar {

    private AppController app;
    private TetrisUIButton soundToggle;
    private TetrisUIButton lightDarkToggle;

    private String SOUND_ON_PATH = "/images/sound-on.png";
    private String SOUND_OFF_PATH = "/images/sound-off.png";

    public NavigationBarLower(AppController app) {
        this.app = app;

        setBackground(Color.black);
        addSoundToggleButton(this);
        addLightDarkToggleButton(this);
        updateButtonStates();
    }

    private void addSoundToggleButton(JComponent c) {
        soundToggle = new TetrisUIButton();
        soundToggle.setPreferredSize(new Dimension(40,40));
        soundToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.toggleSound();
                updateButtonStates();
            }
        });
        c.add(soundToggle);
    }

    private void addLightDarkToggleButton(JComponent c) {
        lightDarkToggle = new TetrisUIButton();
        lightDarkToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.toggleLightDark();
                updateButtonStates();
            }
        });
        c.add(lightDarkToggle);
    }

    public void updateButtonStates() {
        String soundIcon = app.hasSound()
                ? SOUND_ON_PATH
                : SOUND_OFF_PATH;
        soundToggle.setIcon(soundIcon);
        String lightDarkString = app.isDark()
                ? "Dark"
                : "Light";
        lightDarkToggle.setText(lightDarkString);
    }
}
