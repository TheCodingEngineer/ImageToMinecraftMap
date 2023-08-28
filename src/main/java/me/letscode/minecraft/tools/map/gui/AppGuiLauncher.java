package me.letscode.minecraft.tools.map.gui;

import javax.swing.*;

public class AppGuiLauncher {

	public static void main(String[] args) {
		GuiConverter converter = new GuiConverter();
		
		SwingUtilities.invokeLater(() -> {
			converter.setLocationRelativeTo(null);
			converter.setVisible(true);
		});
	}

}
