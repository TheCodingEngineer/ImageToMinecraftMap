package me.letscode.minecraft.tools.map.gui;

import me.letscode.minecraft.tools.map.gui.utils.*;
import me.letscode.minecraft.tools.map.io.InfoMetadata;
import me.letscode.minecraft.tools.map.io.Resources;
import me.letscode.minecraft.tools.map.palette.MapColorPalette;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class GuiConverter extends JFrame implements PropertyChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = 4337891990937899770L;


    static {
        System.setProperty("java.util.PropertyResourceBundle.encoding", "ISO-8859-1");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot change window style: \n"
                            + StringHelper.toString(e), "Error on initializing",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private BottomPanel bottomPanel;
    private DrawPanel drawPanel;
    private File currentDirectory;
    private boolean saving;

    private ResourceBundle language;

    public GuiConverter() {
        init();
    }

    private void init() {
        try {
            this.language = this.loadLanguageBundle();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred: \n"
                            + StringHelper.toString(e) + "\n\nPlease check your file!", "Error on initializing",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }

        setTitle(this.language.getString("title"));

        setLayout(new BorderLayout());
        setIconImage(Resources.getIconResource("appicon.png").getImage());

        addComponents();

        setPreferredSize(new Dimension(840, 480));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }

        });

        String home = System.getenv("HOME"); // Linux, MacOS (Darwin)
        if (home == null) {
            home = System.getenv("userprofile"); // Windows
        }

        this.currentDirectory = new File(home);
        if (!this.currentDirectory.exists() || this.currentDirectory.isFile()) {
            this.currentDirectory = new File(System.getProperty("user.dir"));
        }

        this.pack();
    }

    private ResourceBundle loadLanguageBundle() throws IOException {
        Locale locale = Locale.getDefault();

        InputStream inputStream = Resources.getLanguageResourceFile("lang", locale);
        if (inputStream == null) {
            inputStream = Resources.getLanguageResourceFile("lang", Locale.ENGLISH);
            if (inputStream == null) {
                throw new FileNotFoundException("Unable to find default english language file");
            }
        }
        return new PropertyResourceBundle(inputStream);
    }

    private void setExportEnabled(MenuHandler handler, boolean enabled) {
        handler.getByActionCommand("export.images").setEnabled(enabled);
        handler.getByActionCommand("export.maps").setEnabled(enabled);
        handler.getByActionCommand("export.archive").setEnabled(enabled);
    }

    private void exitProgram() {
        if (saving) {
            int returnVal = JOptionPane.showConfirmDialog(GuiConverter.this,
                    this.language.getString("unsaved.message"), this.language.getString("unsaved.title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (returnVal == JOptionPane.NO_OPTION) {
                return;
            }
        }
        dispose();
        System.exit(0);
    }

    private void addComponents() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        MenuBuilder.setLanguageBundle(this.language);
        MenuPopupBuilder.setLanguageBundle(this.language);

        MenuListener menuListener;
        var fileBuilder = new MenuBuilder("menu.file", menuListener = (handler, item) -> {
            if (item.getActionCommand() == null) {
                return;
            }
            switch (item.getActionCommand()) {
                case "open": {
                        JFileChooser imageChooser = new JFileChooser(currentDirectory);
                        imageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        imageChooser.setMultiSelectionEnabled(false);
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(language.getString("open.filter"),
                                "png", "jpg", "jpeg");
                        imageChooser.setFileFilter(filter);

                        int returnVal = imageChooser.showDialog(this, language.getString("open.title"));
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            try {
                                currentDirectory = imageChooser.getCurrentDirectory();
                                File selected = imageChooser.getSelectedFile();
                                if (!this.loadImageFile(selected)) {
                                    showMessageDialog("open.error.message", "open.error.title",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    handler.getByActionCommand("close").setEnabled(true);
                                    setExportEnabled(handler, true);
                                }
                            } catch (IOException e) {
                                showMessageDialog("open.error.message", "open.error.title",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    break;
                case "close":
                    this.getBottomPanel().setSelectedImageFile(null);
                    this.getDrawPanel().removeImage();

                    handler.getByActionCommand("close").setEnabled(false);
                    setExportEnabled(handler, false);

                    break;
                case "export.images":
                    exportImages();
                    break;
                case "export.maps":
                    exportSingleMaps();
                    break;
                case "export.archive":
                    exportArchive();
                    break;
                case "quit":
                    exitProgram();
                    break;
            }
        })
		.addMenuItem("menu.file.open", "open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
        .addMenuItem("menu.file.close", "close", true)
        .addMenu(new MenuBuilder("menu.file.export", menuListener)
                .addMenuItem("menu.file.export.images", "export.images", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_DOWN_MASK), true)
                .addMenuItem("menu.file.export.maps", "export.maps", KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK),true)
                .addMenuItem("menu.file.export.archive", "export.archive", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_DOWN_MASK |
                        InputEvent.ALT_DOWN_MASK),true)
        )
		.addSeperator()
		.addMenuItem("menu.file.quit", "quit");

        var editBuilder = new MenuBuilder("menu.edit", (handler, item) -> {
            if (item.getActionCommand() == null) {
                return;
            }

            switch (item.getActionCommand()) {
                case "undo":
                    break;
                case "redo":
                    break;
            }
        })
		.addMenuItem("menu.edit.undo", "undo", Resources.getIconResource("undo16.png"),
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK))
		.addMenuItem("menu.edit.redo", "redo", Resources.getIconResource("redo16.png"),
				KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        var infoBuilder = new MenuBuilder("menu.info", (handler, item) -> {
            if (item.getActionCommand() == null) {
                return;
            }

            switch (item.getActionCommand()) {
                case "projectPage":
                    this.openURL("https://github.com/TheCodingEngineer/ImageToMinecraftMap");
                    break;
                case "showAbout":
                    AboutDialog dialog = new AboutDialog(GuiConverter.this, Resources.getIconResource("appicon.png"), this.language);
                    dialog.showCenter();
                    break;
            }
        })
        .addMenuItem("menu.info.page", "projectPage")
        .addSeperator()
        .addMenuItem("menu.info.about", "showAbout");

        addMenu(menuBar, fileBuilder);
        // addMenu(menuBar, editBuilder);
        addMenu(menuBar, infoBuilder);

        add(bottomPanel = new BottomPanel(this), BorderLayout.SOUTH);
        add(drawPanel = new DrawPanel(this), BorderLayout.CENTER);

        var dropAdapter = (new DropTargetAdapter() {

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                boolean accept = false;
                Transferable t = dtde.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        accept = true;
                        Object data = t.getTransferData(DataFlavor.javaFileListFlavor);
                        if (data instanceof List<?>) {
                            for (var item : (List<?>) data) {
                                if (!(item instanceof File)) {
                                    accept = false;
                                    break;
                                }
                            }
                        }
                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }
                }
                if (accept) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void drop(DropTargetDropEvent dropTargetDropEvent) {
                Transferable t = dropTargetDropEvent.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
                    try {
                        java.util.List<File> paths = new ArrayList<>();
                        Object data = t.getTransferData(DataFlavor.javaFileListFlavor);
                        if (data instanceof List<?>) {
                            for (var item : (List<?>) data) {
                                if (item instanceof File) {
                                    paths.add((File) item);
                                }
                            }
                        }

                        if (paths.isEmpty()) {
                            showMessageDialog("open.error.message", "open.error.title",
                                    JOptionPane.ERROR_MESSAGE);
                        } else if (loadImageFile(paths.get(0))) {
                            fileBuilder.getByActionCommand("close").setEnabled(true);
                            setExportEnabled(fileBuilder, true);
                        } else {
                            showMessageDialog("open.error.message", "open.error.title",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (UnsupportedFlavorException | IOException e) {
                        JOptionPane.showMessageDialog(null, "An error occurred: \n"
                                        + StringHelper.toString(e) + "\n\nPlease check your dropped file!", "Error on dropping",
                                JOptionPane.ERROR_MESSAGE);                    }
                } else {
                    dropTargetDropEvent.rejectDrop();
                }
            }
        });

        drawPanel.setDropTarget(new DropTarget(this.drawPanel, DnDConstants.ACTION_COPY, dropAdapter, true));
    }

    private void exportImages() {
        JFileChooser exportChooser = new JFileChooser(currentDirectory);
        exportChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        exportChooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                language.getString("export.images.filter"), "png");
        exportChooser.setFileFilter(filter);
        exportChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        int returnVal = exportChooser.showDialog(this, language.getString("export.images.title"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                currentDirectory = exportChooser.getCurrentDirectory();
                File selected = exportChooser.getSelectedFile();
                String name = selected.getName();

                int dot = name.lastIndexOf('.');
                int id = 0;
                String base = (dot == -1) ? name : name.substring(0, dot);

                BufferedImage[] slices = this.getDrawPanel().sliceImages();
                for (BufferedImage slice : slices) {
                    File output = new File(selected.getParentFile(), String.format("%s_%d.png", base, id++));
                    ImageIO.write(slice, "png", output);
                }
                showMessageDialog("export.images.done.message",
                        "export.images.done.title", JOptionPane.INFORMATION_MESSAGE, id);
            } catch (IllegalArgumentException e) {
                showMessageDialog("export.error.message.bounds", "export.error.title",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                showMessageDialog("export.error.message.io", "export.error.title",
                        JOptionPane.ERROR_MESSAGE, e.getLocalizedMessage());
            }
        }
    }

    private void exportArchive() {
        JFileChooser exportChooser = new JFileChooser(currentDirectory);
        exportChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        exportChooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                language.getString("export.archive.filter"), "zip");
        exportChooser.setFileFilter(filter);
        exportChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        int returnVal = exportChooser.showDialog(this, language.getString("export.archive.title"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                currentDirectory = exportChooser.getCurrentDirectory();
                File selected = exportChooser.getSelectedFile();

                int result = showOptionDialog("export.archive.question.metadata", "export.archive.question.title",
                        JOptionPane.YES_NO_OPTION);

                InfoMetadata metadata = null;
                if (result == JOptionPane.YES_OPTION) {
                    metadata = this.getDrawPanel().getMetadata();
                }

                BufferedImage[] slices = this.getDrawPanel().sliceImages();
                MapColorPalette[] palettes = Resources.getColorPalettes();
                var palette = palettes[palettes.length - 1];

                startArchiveExportTask(selected, metadata, palette, slices);
            } catch (IllegalArgumentException e) {
                showMessageDialog("export.error.message.bounds", "export.error.title",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                showMessageDialog("export.error.message.io", "export.error.title",
                        JOptionPane.ERROR_MESSAGE, e.getLocalizedMessage());
            }
        }
    }

    private void exportSingleMaps() {
        JFileChooser exportChooser = new JFileChooser(currentDirectory);
        exportChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        exportChooser.setMultiSelectionEnabled(false);
        exportChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        int returnVal = exportChooser.showDialog(this, language.getString("export.maps.title"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                currentDirectory = exportChooser.getCurrentDirectory();
                File directory = exportChooser.getSelectedFile();

                String input = showInputDialog("export.input.id", 1);
                int id = Integer.parseInt(input);

                BufferedImage[] slices = this.getDrawPanel().sliceImages();

                MapColorPalette[] palettes = Resources.getColorPalettes();
                var palette = palettes[palettes.length - 1];

                startExportTask(directory, id, palette, slices);
            } catch (NumberFormatException e) {
                showMessageDialog("export.error.message.number", "export.error.title",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                showMessageDialog("export.error.message.bounds", "export.error.title",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                showMessageDialog("export.error.message.io", "export.error.title",
                        JOptionPane.ERROR_MESSAGE, e.getLocalizedMessage());
            }
        }
    }

    private void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private ProgressMonitor taskMonitor;

    private ExportWorker exportTask;


    private boolean taskEnded = false;

    private boolean loadImageFile(File imageFile) throws  IOException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            return false;
        } else {
            this.getDrawPanel().displayImage(image);
            this.getBottomPanel().setSelectedImageFile(imageFile);

            return true;
        }
    }

    private void startExportTask(File directory, int id, MapColorPalette palette, BufferedImage[] slices) {
        if (exportTask != null && !exportTask.isDone()) {
            return;
        }
        taskEnded = false;
        taskMonitor = new ProgressMonitor(this,  language.getString("export.maps.progress.title"),
                "", 0, slices.length);

        exportTask = new ExportWorker(this, directory, id, palette, slices, false, null);
        exportTask.addPropertyChangeListener(this);
        exportTask.execute();
    }

    private void startArchiveExportTask(File archiveFile, InfoMetadata metadata, MapColorPalette palette, BufferedImage[] slices) {
        if (exportTask != null && !exportTask.isDone()) {
            return;
        }
        taskEnded = false;
        taskMonitor = new ProgressMonitor(this,  language.getString("export.maps.progress.title"),
                "", 0, slices.length);

        exportTask = new ExportWorker(this, archiveFile, 1, palette, slices, true, metadata);
        exportTask.addPropertyChangeListener(this);
        exportTask.execute();
    }



    private void endExportTask()  {
        if (exportTask.isCancelled()) {
            showMessageDialog("export.error.message.cancelled", "export.error.title",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                double time = exportTask.get() / 1000.0;
                showMessageDialog("export.maps.done.message",
                        "export.maps.done.title", JOptionPane.INFORMATION_MESSAGE, exportTask.getSlices().length, time);
            } catch (Exception e) {
                showMessageDialog("export.error.message.generic", "export.error.title",
                        JOptionPane.ERROR_MESSAGE, e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("progress".equalsIgnoreCase(event.getPropertyName()) && taskMonitor != null) {
            int progress = (Integer) event.getNewValue();
            taskMonitor.setProgress(progress);

            String message = String.format(language.getString("export.maps.progress.note"), progress,
                    exportTask.getSlices().length);
            taskMonitor.setNote(message);

            if (exportTask.isDone()) {
                endExportTask();
            }

            if (taskMonitor.isCanceled() && !exportTask.isCancelled()) {
                exportTask.cancel(true);
            }
        }
    }

    private void showMessageDialog(String messageKey, String titleKey, int type, Object...formats) {
    	JOptionPane.showMessageDialog(this, String.format(this.language.getString(messageKey), formats),
				this.language.getString(titleKey), type);
	}

    private int showOptionDialog(String messageKey, String titleKey, int optionType, Object...formats) {
        return JOptionPane.showOptionDialog(this,
                String.format(this.language.getString(messageKey), formats),
                this.language.getString(titleKey),
                optionType,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);


    }


    private String showInputDialog(String messageKey, Object initialValue) {
        return JOptionPane.showInputDialog(this, this.language.getString(messageKey), initialValue);
    }

    private void addMenu(JMenuBar menuBar, MenuBuilder builder) {
        menuBar.add(builder.getJMenu());
    }

    public BottomPanel getBottomPanel() {
        return this.bottomPanel;
    }

    public DrawPanel getDrawPanel() {
        return this.drawPanel;
    }

	public ResourceBundle getLanguage() {
		return language;
	}
}
