package be.howest.sooa.o10.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 *
 * @author Hayk
 */
public class PokemonDialogComboBoxItemListener implements ItemListener {

    private final PokemonComboBoxGraphicsDialog parent;

    public PokemonDialogComboBoxItemListener(PokemonComboBoxGraphicsDialog parent) {
        this.parent = parent;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            parent.fillDetails();
        }
    }

}
