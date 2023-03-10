package me.letscode.map.converter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import me.letscode.map.converter.coloring.ColorTable1_12;
import me.letscode.map.converter.coloring.ColorTable1_16;
import org.jnbt.ByteArrayTag;
import org.jnbt.ByteTag;
import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.NBTOutputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

public class ImageMapConverter {

	public static final int DATA_VERSION = 2578; // 1.16.2

	private File imageInput;
	private File nbtOutput;
	private BufferedImage image;

	public ImageMapConverter(File imageInput, File nbtOutput) {
		this.imageInput = imageInput;
		this.nbtOutput = nbtOutput;
	}

	public ImageMapConverter(BufferedImage image, File nbtOutput) {
		this.image = image;
		this.nbtOutput = nbtOutput;
	}
	
	public File getImageInput() {
		return imageInput;
	}

	public File getNbtOutput() {
		return nbtOutput;
	}
	
	public void convert() throws IOException {
		if (this.image == null){
			this.image = ImageIO.read(this.imageInput);
		}

		if (this.image.getWidth() < 128) {
			throw new IOException("Image width too small (< 128)");
		}

		if (this.image.getHeight() < 128) {
			throw new IOException("Image height too small (< 128)");
		}

		NBTOutputStream out = new NBTOutputStream(new FileOutputStream(this.nbtOutput), true);
		HashMap<String, Tag> data = new HashMap<>();
		// set dimension to invalid id => no update process
		data.put("dimension", new ByteTag("dimension", (byte) 0));
		data.put("height", new ShortTag("height", (short) 128));
		data.put("width", new ShortTag("width", (short) 128));

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
		
		CompoundTag tag = new CompoundTag("data", data);
		
		HashMap<String, Tag> finalMap = new HashMap<>();
		finalMap.put("data", tag);
		finalMap.put("DataVersion", new IntTag("DataVersion", DATA_VERSION));
		
		CompoundTag finalTag = new CompoundTag("", finalMap);
		
		out.writeTag(finalTag);
		out.close();
	}
	
	private byte[] fillArray(BufferedImage image) {
		byte[] array = new byte[16384];
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int z = 0; z < 128; z++){
			for (int x = 0; x < 128; x++) {
				int index = coordsToIndex(x, z);

				if (x < width && z < height) {
					int rgb = image.getRGB(x, z);

					Color color = new Color(rgb);

					array[index] = (byte) ColorTable1_16.getMatchingColor(color).getId();
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
