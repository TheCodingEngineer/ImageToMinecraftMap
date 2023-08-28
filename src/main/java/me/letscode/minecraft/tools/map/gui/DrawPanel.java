package me.letscode.minecraft.tools.map.gui;

import me.letscode.minecraft.tools.map.gui.utils.Coordinates;
import me.letscode.minecraft.tools.map.gui.utils.MenuBuilder;
import me.letscode.minecraft.tools.map.gui.utils.MenuPopupBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.ResourceBundle;

public class DrawPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -2849933354357685029L;

    private static final int DEFAULT_WIDTH = 128;
    private static final int DEFAULT_HEIGHT = 128;

    private GuiConverter parent;

    private Color defaultBackgroundPanelColor = new Color(240, 240, 240);
    private Color defaultBackgroundColor = Color.WHITE;

    private int mouseX, mouseY;
    private double scale = 1.0;

    private BufferedImage image;

    private MenuPopupBuilder panelPopupMenu;
    private ResourceBundle language;

    private boolean showGrid;

    public DrawPanel(GuiConverter parent) {
        this.parent = parent;
        this.language = parent.getLanguage();

        initPopupMenu();

        addMouseWheelListener(event -> {
            if (event.getUnitsToScroll() > 0) {
                scale = Math.max(0.5, scale - 0.05);
            } else {
                scale = Math.min(2.0, scale + 0.05);
            }
            repaint();
            updateLabel();
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent event) {
                mouseX = event.getX();
                mouseY = event.getY();

                updateLabel();
            }

        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
            }
        });
    }


    public Coordinates getTopLeftCorner() {
        int x = (this.getWidth() / 2 - (int) (this.getCanvasWidth() / 2.0 * this.scale));
        int y = (this.getHeight() / 2 - (int) (this.getCanvasHeight() / 2.0 * this.scale));

        return new Coordinates(x, y);
    }

    private void initPopupMenu() {
        panelPopupMenu = new MenuPopupBuilder("popup.panel", (handler, item) -> {
            if (item.getActionCommand() == null) {
                return;
            }

            switch (item.getActionCommand()) {
                case "resetBackground":
                    defaultBackgroundColor = Color.WHITE;
                    repaint();
                    break;
                case "setColor":
                    Color selected = JColorChooser.showDialog(parent, this.language.getString("colorChooser.title"),
                            Color.WHITE);
                    if (selected != null) {
                        defaultBackgroundColor = selected;
                        repaint();
                    }
                    break;
                case "removeImage":
                    removeImage();
                    break;
                case "toggleGrid":
                    toggleGrid();
                    break;
            }
        });
        this.showGrid = true;
        panelPopupMenu.addMenu(
                new MenuBuilder("popup.panel.backgroundColor", panelPopupMenu)
                .addMenuItem("popup.panel.defaultColor", "resetBackground")
                .addMenuItem("popup.panel.setColor", "setColor"))
        .addCheckboxMenuItem("popup.panel.showGrid", "toggleGrid", this.showGrid);

        setComponentPopupMenu(panelPopupMenu.getJPopupMenu());
    }

    private void toggleGrid() {
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) this.panelPopupMenu.getByActionCommand("toggleGrid");
        this.showGrid = checkBoxMenuItem.isSelected();
        this.repaint();
    }

    public void displayImage(BufferedImage image) {
        Objects.requireNonNull(image);

        this.scale = 1.0;
        this.image = image;
        this.repaint();
    }

    public void removeImage() {
        this.image = null;
        this.parent.getBottomPanel().setSelectedImageFile(null);

        this.repaint();
    }


    private void updateLabel() {
        Coordinates coordinates = getTopLeftCorner();
        int xRect = (int) ((mouseX - coordinates.getX()) * 1.0 / scale);
        int yRect = (int) ((mouseY - coordinates.getY()) * 1.0 / scale);

        parent.getBottomPanel().updateLabel(xRect, yRect);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr2d = (Graphics2D) g;

        // reset canvas
        clearRect(gr2d);

        // setup canvas
        goToCenter(gr2d);
        scaleCanvas(gr2d);

        // draw image
        goToTopLeftCanvasCoords(gr2d);
        fillCanvasBackground(gr2d);
        if (this.image != null) {
            drawDisplayImage(gr2d);
            if (this.showGrid) {
                drawGrid(gr2d);
            }
        }
    }

    private void drawGrid(Graphics2D gr2d) {
        gr2d.setColor(new Color(0.15f, 0.15f, 0.15f, 0.8f));
        float[] dashingPattern = {1.5f, 3f};
        gr2d.setStroke(new BasicStroke(0.0F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f,
                dashingPattern, 0.75f));

        for (int x = DEFAULT_WIDTH; x < this.getCanvasWidth(); x += DEFAULT_WIDTH) {
            gr2d.drawLine(x, -10, x, this.getCanvasHeight() + 10);
        }

        for (int y = DEFAULT_HEIGHT; y < this.getCanvasHeight(); y += DEFAULT_HEIGHT) {
            gr2d.drawLine(-10, y, this.getCanvasWidth() + 10, y);
        }
    }


    private int getCanvasHeight() {
        if (this.image == null) {
            return DEFAULT_HEIGHT;
        }
        return this.image.getHeight();
    }

    private int getCanvasWidth() {
        if (this.image == null) {
            return DEFAULT_WIDTH;
        }
        return this.image.getWidth();
    }

    private void goToTopLeftCanvasCoords(Graphics2D gr2d) {
        gr2d.translate(-this.getCanvasWidth() / 2, -this.getCanvasHeight() / 2);
    }

    private void fillCanvasBackground(Graphics2D gr2d) {
        gr2d.fillRect(0, 0, this.getCanvasWidth(), this.getCanvasHeight());
    }

    private void scaleCanvas(Graphics2D gr2d) {
        gr2d.scale(this.scale, this.scale);
    }

    private void goToCenter(Graphics2D gr2d) {
        gr2d.translate(this.getWidth() / 2, this.getHeight() / 2);
    }

    private void clearRect(Graphics2D gr2d) {
        gr2d.setColor(defaultBackgroundPanelColor);
        gr2d.fillRect(0, 0, getWidth(), getHeight());
        gr2d.setColor(defaultBackgroundColor);
    }

    private void drawDisplayImage(Graphics2D gr2d) {
        gr2d.drawImage(this.image, 0, 0, this.defaultBackgroundColor, null);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public BufferedImage[] sliceImages() {
        Objects.requireNonNull(this.image);

        if (this.image.getHeight() % 128 != 0) {
            throw new RuntimeException("Invalid image height; expected a multiple of 128");
        }

        if (this.image.getWidth() % 128 != 0) {
            throw new RuntimeException("Invalid image width; expected a multiple of 128");
        }

        int sliceX = this.image.getWidth() / 128;
        int sliceY = this.image.getHeight() / 128;
        int index = 0;

        BufferedImage[] slices = new BufferedImage[sliceX * sliceY];
        for (int x = 0; x < sliceX; x++) {
            for (int y = 0; y < sliceY; y++) {
                slices[index++] = this.image.getSubimage(x * 128, y * 128, 128, 128);
            }
        }
        return slices;
    }
}
