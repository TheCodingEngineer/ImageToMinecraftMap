package me.letscode.map.converter.gui;

import javax.swing.SwingUtilities;

public class MainGuiConverter {
	
	public static void main(String[] args) {
		GuiConverter converter = new GuiConverter();
		
		SwingUtilities.invokeLater(() -> {
			converter.setLocationRelativeTo(null);
			converter.setVisible(true);
		});
	}

}
