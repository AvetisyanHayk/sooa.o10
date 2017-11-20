package be.howest.sooa.o10.gui;

import java.awt.Graphics;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Hayk
 */
public class WorldImagePanel extends JPanel {

    private static final String IMAGE_PATH = "images/%1$s/world.%1$s";

    private ImageIcon image;

    public WorldImagePanel(ImageType imageType) {
        super();
        String imagePath = String.format(IMAGE_PATH, imageType);
        if (new File(imagePath).exists()) {
            image = new ImageIcon(imagePath);
            super.setSize(image.getIconWidth(), image.getIconHeight());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getImage(), 0, 0, this);
    }
}
