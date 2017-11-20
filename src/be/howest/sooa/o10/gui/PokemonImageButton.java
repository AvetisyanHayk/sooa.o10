package be.howest.sooa.o10.gui;

import be.howest.sooa.o10.domain.Pokemon;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Hayk
 */
public class PokemonImageButton extends JButton {

    private final ImageIcon image;
    private final JPanel imageContainer;
    private final Pokemon pokemon;
    private static final int OFFSET = 20;

    public PokemonImageButton(Pokemon pokemon, JPanel imageContainer) {
        super();
        this.imageContainer = imageContainer;
        this.pokemon = pokemon;
        image = getImageIcon();
        optimizeImage();
        super.setIcon(image);
        super.setSize(imageContainer.getWidth(), imageContainer.getHeight());
    }

    private ImageIcon getImageIcon() {
        String imagePath = pokemon.getImagePath();
        if (imagePath != null && new File(imagePath).exists()) {
            return new ImageIcon(pokemon.getImagePath());
        }
        return new ImageIcon("images/gif/no-image.gif");
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    private void optimizeImage() {
        int currentWidth = image.getIconWidth();
        int currentHeight = image.getIconHeight();
        boolean wider = currentWidth > imageContainer.getWidth() - OFFSET;
        boolean taller = currentHeight > imageContainer.getHeight() - OFFSET;
        checkImageSize(currentWidth, currentHeight, wider, taller);
    }

    private void checkImageSize(final int currentWidth, final int currentHeight,
            final boolean wider, final boolean taller) {
        if (wider || taller) {
            int widthOverlap = currentWidth - imageContainer.getWidth() + OFFSET;
            int heightOverlap = currentHeight - imageContainer.getHeight() + OFFSET;
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

}
