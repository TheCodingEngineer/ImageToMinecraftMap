package me.letscode.map.converter.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BottomPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -9005588261776868971L;
    private GuiConverter parent;

    private JLabel coordsLabel;
    private JLabel selectedFileLabel;


    public BottomPanel(GuiConverter parent) {
        this.parent = parent;

        this.coordsLabel = new JLabel("X: 0 Y: 0");
        this.coordsLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        this.selectedFileLabel = new JLabel();
        this.selectedFileLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        setLayout(new BorderLayout());

        setBackground(new Color(250, 250, 250));

        add(coordsLabel, BorderLayout.WEST);
        add(selectedFileLabel, BorderLayout.EAST);
    }

    public void updateLabel(int mouseX, int mouseY) {
        this.coordsLabel.setText("X: " + mouseX + " Y: " + mouseY);
    }

    public void setSelectedImageFile(File selected) {
        if (selected == null) {
            this.selectedFileLabel.setText("");
        } else {
            this.selectedFileLabel.setText(selected.getAbsolutePath());
        }
    }


}
