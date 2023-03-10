package me.letscode.map.converter.coloring;

public class ImageColor {

    public static int counter = 0;

    private int id;
    private int red;
    private int green;
    private int blue;
    private boolean alpha;

    public ImageColor(int id, int red, int green, int blue) {
        this.id = id;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ImageColor(int red, int green, int blue) {
        this(counter++, red, green, blue);
    }

    public ImageColor(int id, boolean alpha) {
        this.id = id;
        this.alpha = alpha;
    }

    public ImageColor(boolean alpha) {
        this(counter++, alpha);
    }

    public int getId() {
        return id;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

}
