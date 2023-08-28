package me.letscode.minecraft.tools.map.io;

import me.letscode.minecraft.tools.map.palette.MapColorPalette;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public final class Resources {

    private static final String LANG_RESOURCE = "/lang/%s_%s.properties";
    private static final String ICON_RESOURCE = "/icons/%s";

    private static final String PALETTE_RESOURCE = "/palettes/colors_%s.json";

    public static MapColorPalette[] getColorPalettes() throws IOException {
        var versions = new String[] {"1_12", "1_16", "1_17"};

        var palettes = new MapColorPalette[versions.length];
        for (int i = 0; i < versions.length; i++) {
            var inputStream = Resources.class.getResourceAsStream(String.format(PALETTE_RESOURCE, versions[i]));
            palettes[i] = ColorPaletteParser.parseColorPalette(inputStream);
        }
        return palettes;
    }

    public static ImageIcon getIconResource(String icon) {
        return new ImageIcon(Resources.class.getResource(String.format(ICON_RESOURCE, icon)));
    }

    public static InputStream getLanguageResourceFile(String key, Locale locale) {
        return Resources.class.getResourceAsStream(String.format(LANG_RESOURCE, key, locale.getLanguage()));
    }


}
