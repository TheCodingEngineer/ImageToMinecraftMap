package me.letscode.minecraft.tools.map.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.letscode.minecraft.tools.map.palette.MapColorPalette;

import java.io.*;

public final class ColorPaletteParser {

    // GSON
    private static final Gson GSON = new GsonBuilder().create();


    public static MapColorPalette parseColorPalette(File file) throws IOException {
        return GSON.fromJson(new FileReader(file), MapColorPalette.class);
    }

    public static MapColorPalette parseColorPalette(InputStream stream) throws IOException {
        return GSON.fromJson(new InputStreamReader(stream), MapColorPalette.class);
    }




}
