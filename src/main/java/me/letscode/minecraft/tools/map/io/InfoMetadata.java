package me.letscode.minecraft.tools.map.io;

public class InfoMetadata {

    public enum ExportFormat {
        COLUMN_WISE,
        ROW_WISE
    }

    private int width;
    private int height;

    private ExportFormat format;

    public InfoMetadata(int width, int height, ExportFormat format) {
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ExportFormat getFormat() {
        return format;
    }
}
