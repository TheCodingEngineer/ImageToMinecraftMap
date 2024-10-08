package me.letscode.minecraft.tools.map.console;

import me.letscode.minecraft.tools.map.io.ColorPaletteParser;
import me.letscode.minecraft.tools.map.io.ImageMapConverter;
import me.letscode.minecraft.tools.map.palette.MinecraftVersion;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;

public class AppConsoleLauncher {

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Syntax: image2map <Input Image> <Output File> [Target Version]");
            System.exit(1);
            return;
        }

        File inputImage = new File(args[0]);
        File outputData = new File(args[1]);

        MinecraftVersion target = new MinecraftVersion("1.17", 2724);

        if (!inputImage.exists()) {
            System.err.println("Input file doesn't exists");
            System.exit(-1);
            return;
        }

        if (outputData.exists()) {
            System.out.println("Overriding output file");
        }

        try {
            var paletteInStream = AppConsoleLauncher.class.getResourceAsStream("/palettes/colors_1_17.json");

            System.out.println("Start converting ...");
            long start = System.currentTimeMillis();

            var image = ImageIO.read(inputImage);
            var colorPalette = ColorPaletteParser.parseColorPalette(paletteInStream);

            var converter = new ImageMapConverter(image, new FileOutputStream(outputData), colorPalette, target);
            converter.convert();

            long end = System.currentTimeMillis();
            System.out.println("Done in " + (end - start) + " ms. Converted to " + outputData.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error occurred while converting image to NBT data: " + e.getMessage());
            System.exit(-1);
        }

    }

}
