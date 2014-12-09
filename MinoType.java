
import java.awt.Color;
import java.util.Random;

public enum MinoType {
    I, J, L, S, Z, T, O;

    private Color color;
    private int[][] defaultCoors;

    static {
        J.color = Color.blue;
        L.color = Color.orange;
        S.color = Color.green;
        Z.color = Color.red;
        T.color = Color.magenta;
        I.color = Color.cyan;
        O.color = Color.yellow;

        // Note, in all minos besides I and O, the coordinate of
        // rotation is the second coordinate, X.defaultCoors[1]
        J.defaultCoors = new int[][]{{5,0},{4,0},{3,0},{3,-1}};
        L.defaultCoors = new int[][]{{3,0},{4,0},{5,0},{5,-1}};
        S.defaultCoors = new int[][]{{3,0},{4,0},{4,-1},{5,-1}};
        Z.defaultCoors = new int[][]{{5,0},{4,0},{4,-1},{3,-1}};
        T.defaultCoors = new int[][]{{3,0},{4,0},{4,-1},{5,0}};
        // I gets rotated about {defaultCoors[2][0], defaultCoors[2][1]+1}
        I.defaultCoors = new int[][]{{6,0},{5,0},{4,0},{3,0}};
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
