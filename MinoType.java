import java.awt.Color;
import java.util.Random;


public enum MinoType {
    I, J, L, S, Z, T, O;

    private Color color;
    private int[][] defaultCoors;

    static {
        I.color = Color.cyan;
        J.color = Color.blue;
        L.color = Color.orange;
        S.color = Color.green;
        Z.color = Color.red;
        T.color = Color.magenta;
        O.color = Color.yellow;

        I.defaultCoors = new int[][]{{3,0},{4,0},{5,0},{6,0}};
        J.defaultCoors = new int[][]{{3,-1},{3,0},{4,0},{5,0}};
        L.defaultCoors = new int[][]{{3,0},{4,0},{5,0},{5,-1}};
        S.defaultCoors = new int[][]{{3,0},{4,0},{4,-1},{5,-1}};
        Z.defaultCoors = new int[][]{{3,-1},{4,-1},{4,0},{5,0}};
        T.defaultCoors = new int[][]{{3,0},{4,0},{4,-1},{5,0}};
        O.defaultCoors = new int[][]{{4,-1},{5,-1},{4,0},{5,0}};
    }

    public Color getColor() {
        return this.color;
    }

    public int[][] getDefaultCoors() {
        return this.defaultCoors;
    }

    static String[] types = {"I", "J", "L", "S", "Z", "T", "O"};

    public static MinoType getRandomMinoType() {
        int rand = (new Random()).nextInt(types.length);
        return MinoType.valueOf(types[rand]);
    }
}
