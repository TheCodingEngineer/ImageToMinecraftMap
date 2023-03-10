package me.letscode.map.converter.command;

import java.io.File;
import java.io.IOException;

import me.letscode.map.converter.ImageMapConverter;

public class MainCommandLineConverter {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Syntax: image2map <Input Image> <Output File>");
            return;
        }

        File inputImage = new File(args[0]);
        File outputData = new File(args[1]);

        if (!inputImage.exists()) {
            System.out.println("Input file doesn't exists");
            return;
        }

        if (outputData.exists()) {
            System.out.println("Overriding output file");
        }

        try {
            System.out.println("Start converting ...");
            long start = System.currentTimeMillis();

            ImageMapConverter converter = new ImageMapConverter(inputImage, outputData);
            converter.convert();

            long end = System.currentTimeMillis();
            System.out.println("Done in " + (end - start) + " ms. Converted to ");
        } catch (IOException e) {
            System.out.println("Error occurred while converting image to NBT-Map: " + e.getMessage());
        }

    }

}
