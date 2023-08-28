package me.letscode.minecraft.tools.map.palette;

import java.awt.*;
import java.util.List;

public class MapColorPalette {

    private String name;

    private List<String> includes;

    private ColorPaletteVersions versions;

    private List<PaletteBaseColor> colors;

    public class ColorPaletteVersions {

        private MinecraftVersion from;

        private MinecraftVersion to;

        public MinecraftVersion getFrom() {
            return from;
        }

        public MinecraftVersion getTo() {
            return to;
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public ColorPaletteVersions getVersions() {
        return versions;
    }

    public Color[] computeColors() {
        Color[] array = new Color[this.colors.size() * 4];
        for (int i = 0; i < colors.size(); i++)
        {
            PaletteBaseColor baseColor = this.colors.get(i);
            Color base = baseColor.getColor();
            array[i * 4] = shadeColor(base, 0.71f);
            array[i * 4 + 1] = shadeColor(base, 0.86f);
            array[i * 4 + 2] = base;
            array[i * 4 + 3] = shadeColor(base, 0.53f);

        }
        return array;
    }

    private Color shadeColor(Color color, float multiplier) {
        int red = Math.round(color.getRed() * multiplier);
        int green = Math.round(color.getGreen() * multiplier);
        int blue = Math.round(color.getBlue() * multiplier);
        return new Color(red, green, blue, color.getAlpha());
    }



}
