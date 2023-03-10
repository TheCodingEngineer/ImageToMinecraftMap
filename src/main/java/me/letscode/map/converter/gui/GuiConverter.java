package me.letscode.map.converter.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.letscode.map.converter.ImageMapConverter;
import me.letscode.map.converter.gui.utils.*;

public class GuiConverter extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 4337891990937899770L;

    private static final String LANG_RESOURCE = "/lang/%s_%s.properties";
    private static final String ICON_RESOURCE = "/icons/%s";

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
        setIconImage(getResourceIcon("appicon.png").getImage());

        addComponents();

        setPreferredSize(new Dimension(840, 480));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }

        });

        this.currentDirectory = new File(System.getenv("userprofile"));
        if (!this.currentDirectory.exists() || this.currentDirectory.isFile()) {
            this.currentDirectory = new File(System.getProperty("user.dir"));
        }

        this.pack();
    }

    private ResourceBundle loadLanguageBundle() throws IOException {
        Locale locale = Locale.getDefault();

        InputStream inputStream = this.getLanguageFile("lang", locale);
        if (inputStream == null) {
            inputStream = this.getLanguageFile("lang", Locale.ENGLISH);
            if (inputStream == null) {
                throw new FileNotFoundException("Unable to find default english language file");
            }
        }
        return new PropertyResourceBundle(inputStream);
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
        MenuBuilder builder = new MenuBuilder("menu.file", menuListener = (handler, item) -> {
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
                                BufferedImage image = ImageIO.read(selected);
                                if (image == null) {
                                    showMessageDialog("open.error.message", "open.error.title",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    this.getDrawPanel().displayImage(image);
                                    this.getBottomPanel().setSelectedImageFile(selected);

                                    handler.getByActionCommand("close").setEnabled(true);
                                    handler.getByActionCommand("export.images").setEnabled(true);
                                    handler.getByActionCommand("export.maps").setEnabled(true);

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
                    handler.getByActionCommand("export.images").setEnabled(false);
                    handler.getByActionCommand("export.maps").setEnabled(false);
                    break;
                case "export.images": {
                        JFileChooser exportChooser = new JFileChooser(currentDirectory);
                        exportChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        exportChooser.setMultiSelectionEnabled(false);
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                language.getString("export.filter"), "png");
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
                            } catch (RuntimeException e) {
                                showMessageDialog("export.error.message.bounds", "export.error.title",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (IOException e) {
                                showMessageDialog("export.error.message.io", "export.error.title",
                                        JOptionPane.ERROR_MESSAGE, e.getLocalizedMessage());
                            }
                        }
                    }
                    break;
                case "export.maps": {
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
                                int parts = 0;

                                long start = System.currentTimeMillis();
                                BufferedImage[] slices = this.getDrawPanel().sliceImages();

                                for (BufferedImage slice : slices) {
                                    File output = new File(directory, String.format("map_%d.dat", id++));
                                    (new ImageMapConverter(slice, output)).convert();
                                    parts++;
                                }

                                long diff = System.currentTimeMillis() - start;
                                showMessageDialog("export.maps.done.message",
                                        "export.maps.done.title", JOptionPane.INFORMATION_MESSAGE, parts, diff);
                            } catch (NumberFormatException e) {
                                showMessageDialog("export.error.message.number", "export.error.title",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (RuntimeException e) {
                                showMessageDialog("export.error.message.bounds", "export.error.title",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (IOException e) {
                                showMessageDialog("export.error.message.io", "export.error.title",
                                        JOptionPane.ERROR_MESSAGE, e.getLocalizedMessage());
                            }
                        }
                    }
                    break;
                case "quit":
                    exitProgram();
                    break;
            }
        })
		.addMenuItem("menu.file.open", "open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
        .addMenuItem("menu.file.close", "close", true)
        .addMenu(new MenuBuilder("menu.file.export", menuListener)
                .addMenuItem("menu.file.export.images", "export.images", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK), true)
                .addMenuItem("menu.file.export.maps", "export.maps", KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK),true))
		.addSeperator()
		.addMenuItem("menu.file.quit", "quit");

        MenuBuilder editBuilder = new MenuBuilder("menu.edit", new MenuListener() {

            @Override
            public void clicked(MenuHandler handler, JMenuItem item) {
                if (item.getActionCommand() == null) {
                    return;
                }

                switch (item.getActionCommand()) {
                    case "undo":
                        break;
                    case "redo":
                        break;
                }
            }
        })
		.addMenuItem("menu.edit.undo", "undo", getResourceIcon("undo16.png"),
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK))
		.addMenuItem("menu.edit.redo", "redo", getResourceIcon("redo16.png"),
				KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        addMenu(menuBar, builder);
        addMenu(menuBar, editBuilder);

        add(bottomPanel = new BottomPanel(this), BorderLayout.SOUTH);
        add(drawPanel = new DrawPanel(this), BorderLayout.CENTER);
    }

    private ImageIcon getResourceIcon(String name) {
        return new ImageIcon(this.getClass().getResource(String.format(ICON_RESOURCE, name)));
    }

    private InputStream getLanguageFile(String key, Locale locale) {
        return this.getClass().getResourceAsStream(String.format(LANG_RESOURCE, key, locale.getLanguage()));
    }

    private void showMessageDialog(String messageKey, String titleKey, int type, Object...formats) {
    	JOptionPane.showMessageDialog(this, String.format(this.language.getString(messageKey), formats),
				this.language.getString(titleKey), type);
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
