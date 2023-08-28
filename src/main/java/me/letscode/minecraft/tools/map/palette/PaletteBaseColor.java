package me.letscode.minecraft.tools.map.palette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaletteBaseColor {

    private List<Integer> base;

    private boolean alpha = false;

    public boolean isAlpha() {
        return alpha;
    }

    public PaletteBaseColor() {
        this.base = List.of(0, 0, 0);
    }

    public Color getColor() {
        if (this.base.size() != 3) {
            throw new IllegalArgumentException();
        }
        int red = base.get(0);
        int green = base.get(1);
        int blue = base.get(2);

        return new Color(red, green, blue, this.isAlpha() ? 255 : 0);
    }


}
