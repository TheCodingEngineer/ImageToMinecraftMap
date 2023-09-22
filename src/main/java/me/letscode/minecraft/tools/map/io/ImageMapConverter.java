package me.letscode.minecraft.tools.map.io;

import me.letscode.minecraft.tools.map.palette.ColorMatcherAlgorithms;
import me.letscode.minecraft.tools.map.palette.MapColorPalette;
import me.letscode.minecraft.tools.map.palette.MinecraftVersion;
import org.jnbt.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageMapConverter {

    private static final int DATA_VERSION_1_16 = 2566;
    private static final UUID DUMMY_WORLD_ID = UUID.fromString("4a12f3b0-fe6b-4000-0000-000000000000");

    private static final byte DUMMY_DIMENSION_ID = 5;

    private File outputFile;
    private BufferedImage image;
    private final MapColorPalette colorPalette;

    private final MinecraftVersion targetVersion;

    public ImageMapConverter(BufferedImage inputImage, File outputFile,
                             MapColorPalette palette, MinecraftVersion target) {
        this.image = inputImage;
        this.outputFile = outputFile;
        this.colorPalette = palette;
        this.targetVersion = target;
    }

    public void convert() throws IOException {
        if (this.image.getWidth() < 128) {
            throw new IllegalArgumentException("Image width too small (< 128)");
        }

        if (this.image.getHeight() < 128) {
            throw new IllegalArgumentException("Image height too small (< 128)");
        }

        NBTOutputStream out = new NBTOutputStream(new FileOutputStream(this.outputFile), true);
        Map<String, Tag> data = new HashMap<>();
        // set dimension to invalid id => no update process (has to be lower than 10)
        // 1.16+ new UUID format
        if (targetVersion.getDataVersion() >= DATA_VERSION_1_16) {
            data.put("UUIDLeast", new LongTag("UUIDLeast", DUMMY_WORLD_ID.getLeastSignificantBits()));
            data.put("UUIDMost",  new LongTag("UUIDMost",  DUMMY_WORLD_ID.getMostSignificantBits()));
            data.put("dimension", new StringTag("dimension", "minecraft:overworld"));
        } else {
            data.put("dimension", new ByteTag("dimension", DUMMY_DIMENSION_ID));
        }

        // minimum scale => doesn't matter with custom image
        data.put("scale", new ByteTag("scale", (byte) 0));

        data.put("xCenter", new IntTag("xCenter", 0));
        data.put("zCenter", new IntTag("zCenter", 0));

        // don't display player markers on the map
        data.put("unlimitedTracking", new ByteTag("unlimitedTracking", (byte) 0));
        data.put("trackingPosition", new ByteTag("trackingPosition", (byte) 0));

        // prevent server from updating the map
        data.put("locked", new ByteTag("locked", (byte) 1));

        // fill colors array
        byte[] colorsArray = fillArray(this.image);
        ByteArrayTag colors = new ByteArrayTag("colors", colorsArray);
        data.put("colors", colors);

        // ignore banners + frames

        CompoundTag tag = new CompoundTag("data", data);

        HashMap<String, Tag> finalMap = new HashMap<>();
        finalMap.put("data", tag);
        finalMap.put("DataVersion", new IntTag("DataVersion", targetVersion.getDataVersion()));

        CompoundTag finalTag = new CompoundTag("", finalMap);

        out.writeTag(finalTag);
        out.close();
    }

    private byte[] fillArray(BufferedImage image) {
        byte[] array = new byte[16384];

        int width = image.getWidth();
        int height = image.getHeight();

        Color[] colors = this.colorPalette.computeColors();

        for (int z = 0; z < 128; z++){
            for (int x = 0; x < 128; x++) {
                int index = coordsToIndex(x, z);

                if (x < width && z < height) {
                    int rgb = image.getRGB(x, z);
                    Color pixel = new Color(rgb);

                    array[index] = (byte) ColorMatcherAlgorithms.findMostSimilarColor(pixel, colors);
                } else {
                    array[index] = 0;
                }
            }
        }
        return array;
    }

    private int coordsToIndex(int x, int z){
        return z * 128 + x;
    }
}
