package me.letscode.minecraft.tools.map.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class BottomPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -9005588261776868971L;
    private final GuiConverter parent;

    private JLabel coordsLabel;
    private JLabel selectedFileLabel;


    public BottomPanel(GuiConverter parent) {
        this.parent = parent;
        this.initialize();
    }

    private void initialize() {
        this.coordsLabel = new JLabel("X: 0 Y: 0");
        this.coordsLabel.setForeground(Color.BLACK);
        this.coordsLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        this.selectedFileLabel = new JLabel();
        this.selectedFileLabel.setForeground(Color.BLACK);
        this.selectedFileLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        add(coordsLabel, BorderLayout.WEST);
        add(selectedFileLabel, BorderLayout.EAST);
    }

    public void updateLabel(int mouseX, int mouseY, int cols, int rows, int width, int height) {
        String text = String.format("X: %d Y: %d - %dx%d  (%dx%d px)", mouseX, mouseY, cols, rows, width, height);
        this.coordsLabel.setText(text);
    }

    public void setSelectedImageFile(File selected) {
        String path = selected == null ? "" : selected.getAbsolutePath();
        this.selectedFileLabel.setText(path);
    }


}
