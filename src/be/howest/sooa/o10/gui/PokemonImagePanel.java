package be.howest.sooa.o10.gui;

import be.howest.sooa.o10.domain.Pokemon;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Hayk
 */
public class PokemonImagePanel extends JPanel {

    private static final String IMAGE_PATH = "images/%1$s/%2$03d.%1$s";

    private ImageIcon image;
    private final JPanel imageContainer;

    public PokemonImagePanel(String imagePath, JPanel imageContainer) {
        super();
        this.imageContainer = imageContainer;
        if (new File(imagePath).exists()) {
            image = new ImageIcon(imagePath);
            optimizeImage();
        }
    }

    private void optimizeImage() {
        int currentWidth = image.getIconWidth();
        int currentHeight = image.getIconHeight();
        boolean wider = currentWidth > imageContainer.getWidth();
        boolean taller = currentHeight > imageContainer.getHeight();
        checkImageSize(currentWidth, currentHeight, wider, taller);
    }

    private void checkImageSize(final int currentWidth, final int currentHeight,
            final boolean wider, final boolean taller) {
        if (wider || taller) {
            int widthOverlap = currentWidth - imageContainer.getWidth();
            int heightOverlap = currentHeight - imageContainer.getHeight();
            if (wider && taller) {
                scaleOutImage(currentWidth, currentHeight, widthOverlap, heightOverlap);
            } else if (wider) {
                scaleOutByWidth(currentWidth, currentHeight, widthOverlap);
            } else {
                scaleOutByHeight(currentWidth, currentHeight, heightOverlap);
            }
        }
    }

    private void scaleOutImage(final int oldWidth, final int oldHeight,
            final int widthOverlap, final int heightOverlap) {
        int newWidth;
        int newHeight;
        if (widthOverlap > heightOverlap) {
            newWidth = oldWidth - widthOverlap - 1;
            newHeight = (int) ((newWidth / (double) oldWidth) * oldHeight);
        } else {
            newHeight = oldHeight - heightOverlap - 1;
            newWidth = (int) ((newHeight / (double) oldHeight) * oldWidth);
        }
        scale(newWidth, newHeight);
    }

    private void scaleOutByWidth(final int oldWidth, final int oldHeight,
            final int widthOverlap) {
        int newWidth = oldWidth - widthOverlap - 1;
        int newHeight = (int) ((newWidth / (double) oldWidth) * oldHeight);
        scale(newWidth, newHeight);
    }

    private void scaleOutByHeight(final int oldWidth, final int oldHeight,
            final int heightOverlap) {
        int newHeight = oldHeight - heightOverlap - 1;
        int newWidth = (int) ((newHeight / (double) oldHeight) * oldWidth);
        scale(newWidth, newHeight);
    }

    private void scale(int newWidth, int newHeight) {
        image.setImage(image.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (imageContainer.getWidth() - image.getIconWidth()) / 2;
        int y = (imageContainer.getHeight() - image.getIconHeight()) / 2;
        g.drawImage(image.getImage(), x, y, this);
    }

    public static boolean imageExistsFor(Pokemon pokemon, ImageType imageType) {
        String path = String.format(IMAGE_PATH, imageType,
                pokemon.getSpeciesId());
        return new File(path).exists();
    }

    public static String getImagePathFor(Pokemon pokemon, ImageType imageType) {
        String path = String.format(IMAGE_PATH, imageType,
                pokemon.getSpeciesId());
        if (new File(path).exists()) {
            return path;
        }
        return null;
    }
}
