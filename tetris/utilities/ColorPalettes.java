package tetris.utilities;
import java.awt.Color;
import java.util.HashMap;

public class ColorPalettes {

    public static HashMap<ColorRole,Color> getLightPalette() {
        HashMap<ColorRole,Color> palette = new HashMap<ColorRole,Color>();
        palette.put(ColorRole.APP_BACKGROUND, new Color(247,247,247));
        palette.put(ColorRole.TEXT_COLOR, Color.black);
        palette.put(ColorRole.LIGHT_ACCENT, new Color(236,236,236));
        palette.put(ColorRole.DARK_ACCENT, new Color(206,206,206));
        palette.put(ColorRole.MATRIX_DARK, new Color(140,140,140,128));
        palette.put(ColorRole.MATRIX_LIGHT, new Color(150,150,150,128));
        return palette;
    }

    public static HashMap<ColorRole,Color> getDarkPalette() {
        HashMap<ColorRole,Color> palette = new HashMap<ColorRole,Color>();
        palette.put(ColorRole.APP_BACKGROUND, new Color(24,24,24));
        palette.put(ColorRole.TEXT_COLOR, Color.white);
        palette.put(ColorRole.LIGHT_ACCENT, new Color(32,32,32));
        palette.put(ColorRole.DARK_ACCENT, new Color(48,48,48));
        palette.put(ColorRole.MATRIX_DARK, new Color(40,40,40,128));
        palette.put(ColorRole.MATRIX_LIGHT, new Color(50,50,50,128));
        return palette;
    }
}
