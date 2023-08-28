package me.letscode.minecraft.tools.map.gui.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringHelper {

    public static String toString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
