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

    private static final int GRID_WIDTH = 128;
    private static final int GRID_HEIGHT = 128;

    private final GuiConverter parent;

    private final Color defaultBackgroundPanelColor = new Color(240, 240, 240);
    private Color defaultBackgroundColor = Color.WHITE;

    private int mouseX, mouseY;


    private int dragOriginX, dragOriginY;

    private boolean draggable;

    private int offsetX, offsetY;
    private double scale = 1.0;

    private int canvasCols;

    private int canvasRows;

    private BufferedImage displayImage;

    private MenuPopupBuilder panelPopupMenu;
    private ResourceBundle language;

    private boolean showGrid;

    public DrawPanel(GuiConverter parent) {
        this.parent = parent;
        this.language = parent.getLanguage();

        this.setBounds(1, 1);
        this.initialize();
    }

    private void initialize() {
        initPopupMenu();

        addMouseWheelListener(event -> {
            if (event.getUnitsToScroll() > 0) {
                scale = Math.max(0.2, scale - 0.05);
            } else {
                scale = Math.min(2.0, scale + 0.05);
            }
            repaint();
            updateLabel();
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent event) {
                if (draggable) {
                    offsetX = dragOriginX - event.getX();
                    offsetY = dragOriginY - event.getY();

                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                mouseX = event.getX();
                mouseY = event.getY();

                updateLabel();
            }

        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    draggable = true;
                    dragOriginX = event.getX() + offsetX;
                    dragOriginY = event.getY() + offsetY;
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    draggable = false;
                }
            }

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

    public void setBounds(int canvasCols, int canvasRows)  {
        this.canvasCols = canvasCols;
        this.canvasRows = canvasRows;
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

        int rows = (int) Math.ceil((double) image.getHeight() / GRID_HEIGHT);
        int cols = (int) Math.ceil((double) image.getWidth() / GRID_HEIGHT);

        this.setBounds(cols, rows);

        this.scale = 1.0;
        this.displayImage = image;
        this.repaint();
        this.updateLabel();
    }

    public void removeImage() {
        this.displayImage = null;
        this.parent.getBottomPanel().setSelectedImageFile(null);

        this.setBounds(1, 1);
        this.offsetY = 0;
        this.offsetX = 0;
        this.draggable = false;
        this.scale = 1.0;

        this.repaint();
        this.updateLabel();
    }


    private void updateLabel() {
        Coordinates coordinates = getTopLeftCorner();
        int xRect = (int) ((mouseX + offsetX - coordinates.getX()) * 1.0 / scale);
        int yRect = (int) ((mouseY + offsetY - coordinates.getY()) * 1.0 / scale);

        parent.getBottomPanel().updateLabel(xRect, yRect, canvasCols, canvasRows, getCanvasWidth(), getCanvasHeight());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr2d = (Graphics2D) g;

        // reset canvas
        clearRect(gr2d);

        // setup canvas
        goToOrigin(gr2d);
        scaleCanvas(gr2d);

        // draw image
        goToTopLeftCanvasCoords(gr2d);
        fillCanvasBackground(gr2d);
        if (this.displayImage != null) {
            drawDisplayImage(gr2d);
        }
        if (this.showGrid) {
            drawGrid(gr2d);
        }
    }

    private BufferedImage paintImage() {
        BufferedImage image = new BufferedImage(getCanvasWidth(), getCanvasHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gr2d = (Graphics2D) image.getGraphics();

        fillCanvasBackground(gr2d);
        if (this.displayImage != null) {
            drawDisplayImage(gr2d);
        }
        return image;
    }

    private void drawGrid(Graphics2D gr2d) {
        gr2d.setColor(new Color(0.15f, 0.15f, 0.15f, 0.8f));
        float[] dashingPattern = {1.5f, 3f};
        gr2d.setStroke(new BasicStroke(0.0F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f,
                dashingPattern, 0.75f));

        for (int x = GRID_WIDTH; x < this.getCanvasWidth(); x += GRID_WIDTH) {
            gr2d.drawLine(x, -10, x, this.getCanvasHeight() + 10);
        }

        for (int y = GRID_HEIGHT; y < this.getCanvasHeight(); y += GRID_HEIGHT) {
            gr2d.drawLine(-10, y, this.getCanvasWidth() + 10, y);
        }
    }


    private int getCanvasHeight() {
        return this.canvasRows * GRID_HEIGHT;
    }

    private int getCanvasWidth() {
        return this.canvasCols * GRID_WIDTH;
    }

    private void goToTopLeftCanvasCoords(Graphics2D gr2d) {
        gr2d.translate(-this.getCanvasWidth() / 2, -this.getCanvasHeight() / 2);
    }

    private void fillCanvasBackground(Graphics2D gr2d) {
        gr2d.setColor(defaultBackgroundColor);
        gr2d.fillRect(0, 0, this.getCanvasWidth(), this.getCanvasHeight());
    }

    private void scaleCanvas(Graphics2D gr2d) {
        gr2d.scale(this.scale, this.scale);
    }

    private void goToCenter(Graphics2D gr2d) {
        gr2d.translate(this.getWidth() / 2, this.getHeight() / 2);
    }

    private void goToOrigin(Graphics2D gr2d) {
        gr2d.translate(this.getWidth() / 2 - this.offsetX, this.getHeight() / 2 - this.offsetY);
    }

    private void clearRect(Graphics2D gr2d) {
        gr2d.setColor(defaultBackgroundPanelColor);
        gr2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawDisplayImage(Graphics2D gr2d) {
        int offsetX = (this.getCanvasWidth() - this.displayImage.getWidth()) / 2;
        int offsetY = (this.getCanvasHeight() - this.displayImage.getHeight()) / 2;

        gr2d.drawImage(this.displayImage, offsetX, offsetY, this.defaultBackgroundColor, null);
    }

    public BufferedImage[] sliceImages() {
        BufferedImage image = this.paintImage();

        if (image.getHeight() % GRID_HEIGHT != 0) {
            throw new IllegalArgumentException("Invalid image height; expected a multiple of " + GRID_HEIGHT);
        }

        if (image.getWidth() % GRID_WIDTH != 0) {
            throw new IllegalArgumentException("Invalid image width; expected a multiple of " + GRID_WIDTH);
        }

        int sliceX = image.getWidth() / GRID_WIDTH;
        int sliceY = image.getHeight() / GRID_HEIGHT;
        int index = 0;

        BufferedImage[] slices = new BufferedImage[sliceX * sliceY];
        for (int x = 0; x < sliceX; x++) {
            for (int y = 0; y < sliceY; y++) {
                slices[index++] = image.getSubimage(x * GRID_WIDTH, y * GRID_HEIGHT, GRID_WIDTH, GRID_HEIGHT);
            }
        }
        return slices;
    }
}
