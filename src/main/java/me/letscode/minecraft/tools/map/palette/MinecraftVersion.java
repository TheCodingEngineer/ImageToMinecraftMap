package me.letscode.minecraft.tools.map.palette;

public class MinecraftVersion {

    private String name;

    private int dataVersion;

    public MinecraftVersion() {}

    public MinecraftVersion(String name, int dataVersion) {
        this.name = name;
        this.dataVersion = dataVersion;
    }

    public String getName() {
        return name;
    }

    public int getDataVersion() {
        return dataVersion;
    }
}
