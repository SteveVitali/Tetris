
import java.awt.Color;
import java.awt.Graphics;

public class Mino {
    private MinoType type;
    protected Color color;
    private int[][] coors;

    // Default constructor instantiates random mino type
    public Mino() {
        this.type  = MinoType.getRandomMinoType();
        this.color = type.getColor();
        this.coors = getCoorCopy(type.getDefaultCoors());
    }

    public Mino(MinoType type) {
        this.type  = type;
        this.color = type.getColor();
        this.coors = getCoorCopy(type.getDefaultCoors());
    }

    public void move(int x, int y) {
        for (int[] coor : coors) {
            coor[0] += x;
            coor[1] += y;
        }
    }

    public Color getColor() {
        return color;
    }

    private int[][] getCoorCopy(int[][] someCoors) {
        int[][] copyCoors = new int[4][2];
        for (int i=0; i<someCoors.length; i++) {
            for (int j=0; j<someCoors[i].length; j++) {
                copyCoors[i][j] = someCoors[i][j];
            }
        }
        return copyCoors;
    }

    public int[][] getCoors() {
        return getCoorCopy(coors);
    }

    public MinoType getType() {
        return type;
    }

    public void setCoors(int[][] newCoors) {
        this.coors = getCoorCopy(newCoors);
    }

    public int[][] rotateClockwise() {
        return rotate(getCoorCopy(coors), true);
    }

    public int[][] rotateCounterClockwise() {
        return rotate(getCoorCopy(coors), false);
    }

    public int[][] rotate(int[][] coordinates, boolean clockwise) {
        switch (type) {
        case O:
            return coordinates;
        default:
            int[][] rotated = new int[4][2];
            int[] origin = coordinates[1];
            for (int i=0; i<coordinates.length; i++) {
                int[] coor = coordinates[i];
                int[] translated = new int[]{
                        coor[0] - origin[0], coor[1] - origin[1]
                };
                translated[1] *= clockwise ? 1 : -1;
                rotated[i] = new int[]{translated[0], translated[1]};
                rotated[i][0] = (int)Math.round(-1*translated[1]);
                rotated[i][1] = (int)Math.round(translated[0]);
                rotated[i][1] *= clockwise ? 1 : -1;
                rotated[i][0] += origin[0];
                rotated[i][1] += origin[1];
            }
            return rotated;
        }
    }

    public void draw(Graphics g, int width) {
        for (int[] coor : coors) {
            g.setColor(color);
            g.fillRect(coor[0]*width, coor[1]*width, width, width);

            // Add white/black outline for shadow effect
            g.setColor(Color.white);
            g.drawRect(coor[0]*width, coor[1]*width, width-1, width-1);

            g.setColor(Color.black);
            g.drawRect(coor[0]*width, coor[1]*width, width, width);
        }
    }

    public void drawInRect(Graphics g, int width, int height) {
        // Lots of math to center the mino in a given rectangle
        int maxX = 0, minX = Integer.MAX_VALUE;
        int maxY = 0, minY = Integer.MAX_VALUE;
        for (int[] coor : coors) {
            maxX = coor[0] > maxX ? coor[0] : maxX;
            minX = coor[0] < minX ? coor[0] : minX;
            maxY = coor[1] > maxY ? coor[1] : maxY;
            minY = coor[1] < minY ? coor[1] : minY;
        }
        int minoWidth  = maxX - minX + 1;
        int minoHeight = maxY - minY + 1;

        int outerBoxWidth = width < height ? width : height;
        int padding = 4;
        int boundingBoxWidth = outerBoxWidth - padding * 2;
        int minoBlockWidth = boundingBoxWidth / 4;

        int xOffset = (width - minoBlockWidth  * minoWidth ) / 2;
        int yOffset = (height - minoBlockWidth * minoHeight) / 2;

        for (int[] coor : coors) {
            int x = (coor[0] - minX) * minoBlockWidth + xOffset;
            int y = (coor[1] - minY) * minoBlockWidth + yOffset;

            g.setColor(color);
            g.fillRect(x, y, minoBlockWidth, minoBlockWidth);
            g.setColor(Color.white);
            g.drawRect(x, y, minoBlockWidth-1, minoBlockWidth-1);
            g.setColor(Color.black);
            g.drawRect(x, y, minoBlockWidth, minoBlockWidth);
        }
    }
}
