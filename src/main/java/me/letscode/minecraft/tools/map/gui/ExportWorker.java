package me.letscode.minecraft.tools.map.gui;

import me.letscode.minecraft.tools.map.io.ImageMapConverter;
import me.letscode.minecraft.tools.map.palette.MapColorPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportWorker extends SwingWorker<Long, Void> {

    private final GuiConverter parent;

    private final BufferedImage[] slices;

    private final File exportDir;

    private final MapColorPalette colorPalette;

    private final int startId;

    public ExportWorker(GuiConverter parent, File exportDir, int startId, MapColorPalette colorPalette, BufferedImage[] slices) {
        this.parent = parent;
        this.slices = slices;
        this.colorPalette = colorPalette;
        this.startId = startId;
        this.exportDir = exportDir;
    }

    @Override
    protected Long doInBackground() throws Exception {
        int id = startId;
        var version = this.colorPalette.getVersions().getFrom();
        setProgress(0);
        long start = System.currentTimeMillis();
        for (BufferedImage slice : slices) {
            if (isCancelled()) {
                break;
            }
            String file = String.format("map_%d.dat", id++);
            File output = new File(this.exportDir, file);

            (new ImageMapConverter(slice, output, this.colorPalette, version)).convert();

            setProgress(id - startId);
        }
        return (System.currentTimeMillis() - start);
    }

    public BufferedImage[] getSlices() {
        return slices;
    }

    @Override
    protected void done() {
        Toolkit.getDefaultToolkit().beep();
    }
}
