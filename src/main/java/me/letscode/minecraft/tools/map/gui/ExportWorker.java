package me.letscode.minecraft.tools.map.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.letscode.minecraft.tools.map.io.ImageMapConverter;
import me.letscode.minecraft.tools.map.io.InfoMetadata;
import me.letscode.minecraft.tools.map.palette.MapColorPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ExportWorker extends SwingWorker<Long, Void> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final GuiConverter parent;

    private final BufferedImage[] slices;

    private final File file;

    private final MapColorPalette colorPalette;

    private final int startId;

    // Archive Options
    private boolean isArchive;

    private InfoMetadata infoMetadata;

    public ExportWorker(GuiConverter parent, File file, int startId, MapColorPalette colorPalette,
                        BufferedImage[] slices, boolean isArchive, InfoMetadata metadata) {
        this.parent = parent;
        this.slices = slices;
        this.colorPalette = colorPalette;
        this.startId = startId;
        this.file = file;
        this.isArchive = isArchive;
        this.infoMetadata = metadata;
    }

    @Override
    protected Long doInBackground() throws Exception {
        int id = startId;
        var version = this.colorPalette.getVersions().getFrom();
        setProgress(0);
        long start = System.currentTimeMillis();

        if (isArchive) {
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(this.file))) {
                for (BufferedImage slice : slices) {
                    if (isCancelled()) {
                        break;
                    }
                    String file = String.format("map_%d.dat", id++);
                    ZipEntry mapEntry = new ZipEntry(file);
                    zos.putNextEntry(mapEntry);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    (new ImageMapConverter(slice, bos, this.colorPalette, version)).convert();
                    zos.write(bos.toByteArray());

                    zos.closeEntry();
                    setProgress(id - startId);
                }

                if (infoMetadata != null) {
                    ZipEntry entry = new ZipEntry("info.json");
                    zos.putNextEntry(entry);
                    zos.write(GSON.toJson(infoMetadata).getBytes(StandardCharsets.UTF_8));
                    zos.closeEntry();
                }
            }
        } else {
            for (BufferedImage slice : slices) {
                if (isCancelled()) {
                    break;
                }
                String file = String.format("map_%d.dat", id++);
                File mapFile = new File(this.file, file);

                (new ImageMapConverter(slice, new FileOutputStream(mapFile), this.colorPalette, version))
                        .convert();

                setProgress(id - startId);
            }
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
