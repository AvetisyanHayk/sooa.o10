package be.howest.sooa.o10.gui;

import be.howest.sooa.o10.data.EncounterRepository;
import be.howest.sooa.o10.data.PokemonRepository;
import be.howest.sooa.o10.domain.Encounter;
import be.howest.sooa.o10.domain.Location;
import be.howest.sooa.o10.domain.Pokemon;
import be.howest.sooa.o10.domain.Trainer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Hayk
 */
public class MainFrame extends javax.swing.JFrame {

    private transient PokemonRepository pokemonRepo;
    private transient EncounterRepository encounterRepo;
    private PokemonSelectorDialog pokemonSelectorDialog;
    private transient Trainer trainer;
    private List<Encounter> encounters;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    public void confirmAuthentication() {
        pokemonRepo = new PokemonRepository();
        init();
    }

    public void init() {
        pokemonSelectorDialog = new PokemonSelectorDialog(this);
        centerScreen(pokemonSelectorDialog);
        addDialogKeyListener(pokemonSelectorDialog);
        addListeners();
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public boolean hasTrainer() {
        return trainer != null;
    }

    public void load() {
        if (encounterRepo == null) {
            encounterRepo = new EncounterRepository();
        }
        fillEncounters();
        setTrainerPokemonsImagePath();
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void addListeners() {
        addExitButtonActionListener();
        addTrainerButtonActionListener();
        addSelectPokemonButtonActionListener();
    }

    private void addExitButtonActionListener() {
        exitButton.addActionListener((ActionEvent e) -> {
            close();
        });
    }

    private void addTrainerButtonActionListener() {
        trainerButton.addActionListener((ActionEvent e) -> {
            removeMouseClickListener();
            TrainerDialog dialog = new TrainerDialog(this);
            centerScreen(dialog);
            addDialogKeyListener(dialog);
            dialog.setVisible(true);
        });
    }

    private void addSelectPokemonButtonActionListener() {
        selectPokemonButton.addActionListener((ActionEvent e) -> {
            removeMouseClickListener();
            findAllPokemons(false);
            pokemonSelectorDialog.setVisible(true);
        });
    }

    private void addMouseClickListener(Pokemon pokemon) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getLocationOnScreen();
                if (isPointInsideWorldPanel(point)) {
                    showEncounterDialog(pokemon, getLocationInWorld(point));
                }
                removeMouseClickListener();
            }
        });
    }
    
    private void removeMouseClickListener() {
        MouseListener[] listeners = this.getMouseListeners();
        if (listeners != null) {
            for (MouseListener listener : listeners) {
                this.removeMouseListener(listener);
            }
        }
    }

    private boolean isPointInsideWorldPanel(Point pointOnScreen) {
        Point startPointOnScreen = worldPanel.getLocationOnScreen();
        Point endPointOnScreen
                = new Point(startPointOnScreen.x + worldPanel.getWidth(),
                        startPointOnScreen.y + worldPanel.getHeight());
        return pointOnScreen.x >= startPointOnScreen.x
                && pointOnScreen.x <= endPointOnScreen.x
                && pointOnScreen.y >= startPointOnScreen.y
                && pointOnScreen.y <= endPointOnScreen.y;
    }
    
    private Location getLocationInWorld(Point pointOnScreen) {
        Point startPointOnScreen = worldPanel.getLocationOnScreen();
        int x = pointOnScreen.x - startPointOnScreen.x;
        int y = pointOnScreen.y - startPointOnScreen.y;
        return new Location(x, y);
    }

    public void addDialogKeyListener(JDialog dialog) {
        KeyStroke escapeStroke
                = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        String dispatchWindowClosingActionMapKey
                = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey,
                new DialogClosingOnEscapeAction(dialog));
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Data Manipulation">
    void findAllPokemons(boolean onlyWithImage) {
        List<Pokemon> pokemons = pokemonRepo.findAllWithImagePath(ImageType.GIF);
        if (onlyWithImage) {
            pokemons = pokemons.stream()
                    .filter((pokemon) -> pokemon.getImagePath() != null)
                    .collect(Collectors.toList());
        }
        pokemonSelectorDialog.loadFirstPage(pokemons);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Fill, Clear, Reset, etc.">
    void fillEncounters() {
        encounters = encounterRepo.findAll();
        drawWorld();
    }
    
    void addEncounter(Encounter encounter) {
        encounterRepo.save(encounter);
        fillEncounters();
    }

    private void drawWorld() {
        drawEncounters();
    }

    private void drawEncounters() {

    }

    void selectPokemon(Pokemon pokemon) {
        addMouseClickListener(pokemon);
    }
    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Custom Functions">
    void centerScreen(Window window) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - window.getWidth()) / 2;
        final int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    private void centerScreen() {
        centerScreen(this);
    }

    private void connectToDatabase() {
        DatabaseConnectionDialog dialog = new DatabaseConnectionDialog(this);
        centerScreen(dialog);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                if (!isConnected()) {
                    close();
                }
            }
        });
        dialog.setVisible(true);
    }

    public void selectTrainer() {
        SelectTrainerDialog dialog = new SelectTrainerDialog(this);
        centerScreen(dialog);
        addDialogKeyListener(dialog);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                if (!hasTrainer()) {
                    close();
                }
            }
        });
        dialog.setVisible(true);
    }

    public void close() {
        setVisible(false);
        dispose();
    }

    public boolean isConnected() {
        return pokemonRepo != null;
    }

    public void setTrainerPokemonsImagePath() {
        if (trainer != null) {
            trainer.setPokemons(PokemonRepository
                    .getPokemonsWithImagePath(trainer.getPokemons(),
                            ImageType.GIF));
        }
    }

    private void showEncounterDialog(Pokemon pokemon, Location location) {
        Encounter encounter = new Encounter(pokemon, location);
        EncounterDialog dialog = new EncounterDialog(this, encounter);
        centerScreen(dialog);
        addDialogKeyListener(dialog);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dialog.dispose();
            }
        });
        dialog.setVisible(true);
    }
    // </editor-fold>
    //
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exitButton = new javax.swing.JButton();
        trainerButton = new javax.swing.JButton();
        selectPokemonButton = new javax.swing.JButton();
        worldPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pokedex II");
        setBackground(new java.awt.Color(255, 255, 255));

        exitButton.setText("Exit");

        trainerButton.setText("Trainer...");

        selectPokemonButton.setText("Select Pokémon...");

        worldPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout worldPanelLayout = new javax.swing.GroupLayout(worldPanel);
        worldPanel.setLayout(worldPanelLayout);
        worldPanelLayout.setHorizontalGroup(
            worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        worldPanelLayout.setVerticalGroup(
            worldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 404, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(worldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(trainerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectPokemonButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 597, Short.MAX_VALUE)
                        .addComponent(exitButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(worldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton)
                    .addComponent(trainerButton)
                    .addComponent(selectPokemonButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println(ex.getMessage());
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MainFrame newMainFrame = new MainFrame();
            newMainFrame.centerScreen();
            newMainFrame.setVisible(true);
            newMainFrame.connectToDatabase();
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitButton;
    private javax.swing.JButton selectPokemonButton;
    private javax.swing.JButton trainerButton;
    private javax.swing.JPanel worldPanel;
    // End of variables declaration//GEN-END:variables

    // <editor-fold defaultstate="collapsed" desc="Custom Listeners, Actions, Models">
    private static class DialogClosingOnEscapeAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        final JDialog dialog;

        DialogClosingOnEscapeAction(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispatchEvent(new WindowEvent(
                    dialog, WindowEvent.WINDOW_CLOSING));
        }
    }
    // </editor-fold>

}
