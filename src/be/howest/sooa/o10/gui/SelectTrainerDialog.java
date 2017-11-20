package be.howest.sooa.o10.gui;

import be.howest.sooa.o10.data.TrainerRepository;
import be.howest.sooa.o10.domain.Trainer;
import be.howest.sooa.o10.ex.TrainerIOException;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Hayk
 */
public class SelectTrainerDialog extends javax.swing.JDialog {

    private final MainFrame parent;
    private final transient TrainerRepository trainerRepo= new TrainerRepository();

    public SelectTrainerDialog(MainFrame parent) {
        super(parent, true);
        this.parent = parent;
        initComponents();
        fillTrainersList();
        addListeners();
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    private void addListeners() {
        addExitButtonActionListener();
        addEditButtonActionListener();
        addAddButtonActionListener();
        addSelectButtonActionListener();
        addDeleteButtonActionListener();
    }

    private void addExitButtonActionListener() {
        exitButton.addActionListener((ActionEvent e) -> {
            parent.close();
        });
    }

    private void addEditButtonActionListener() {
        editButton.addActionListener((ActionEvent e) -> {
            Trainer trainer = (Trainer) trainersList.getSelectedItem();
            JDialog dialog = new TrainerFormDialog(this, trainer);
            dialog.setTitle("Edit Trainer");
            parent.centerScreen(dialog);
            parent.addDialogKeyListener(dialog);
            dialog.setVisible(true);
        });
    }

    private void addAddButtonActionListener() {
        addButton.addActionListener((ActionEvent e) -> {
            showAddTrainerDialog();
        });
    }

    private void addSelectButtonActionListener() {
        selectButton.addActionListener((ActionEvent e) -> {
            if (trainersList.getModel().getSize() == 0) {
                showAddTrainerDialog();
            } else {
                Trainer trainer = (Trainer) trainersList.getSelectedItem();
                if (trainer == null) {
                    showWarning("Select trainer, please!");
                } else {
                    parent.setTrainer(trainer);
                    parent.load();
                    setVisible(false);
                    dispose();
                }
            }
        });
    }

    private void addDeleteButtonActionListener() {
        deleteButton.addActionListener((ActionEvent e) -> {
            Object[] options = {"Delete Trainer", "Do not delete"};
            Trainer selected = (Trainer) trainersList.getSelectedItem();
            String message = "Please, confirm you want to delete trainer "
                    + selected.getName();
            int result = JOptionPane.showOptionDialog(this, message,
                    "Delete trainer", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (result == 0) {
                trainerRepo.remove(selected);
                fillTrainersList();
                if (trainersList.getModel().getSize() == 0) {
                    showAddTrainerDialog();
                }
            }
        });
    }

    private void showAddTrainerDialog() {
        JDialog dialog = new TrainerFormDialog(this);
        dialog.setTitle("Add Trainer");
        parent.centerScreen(dialog);
        parent.addDialogKeyListener(dialog);
        dialog.setVisible(true);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Data Manipulation">
    public void saveTrainer(Trainer trainer) {
        if (trainer != null) {
            Trainer checkTrainer = trainerRepo.findByName(trainer.getName());
            if (checkTrainer != null) {
                showWarning("Trainer " + trainer.getName() + " already exists!");
            } else {
                try {
                    trainerRepo.save(trainer);
                } catch (TrainerIOException ex) {
                    showWarning(ex.getMessage());
                }
            }
            fillTrainersList();
            trainersList.setSelectedItem(trainer);
        }
    }

    public void updateTrainerName(Trainer trainer, String oldTrainerName) throws TrainerIOException {
        if (trainer != null && oldTrainerName != null && !"".equals(oldTrainerName)) {
            try {
                trainerRepo.updateTrainerName(trainer, oldTrainerName);
            } catch (TrainerIOException ex) {
                showWarning(ex.getMessage());
            }
            fillTrainersList();
            trainersList.setSelectedItem(trainer);
        }
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Fill">
    private void fillTrainersList() {
        try {
            List<Trainer> trainers = trainerRepo.findAll();
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            trainers.forEach((trainer) -> {
                model.addElement(trainer);
            });
            trainersList.setModel(model);
            if (trainers.isEmpty()) {
                showAddTrainerDialog();
            }
        } catch (TrainerIOException ex) {
            showWarning(ex.getMessage());
        }
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Custom Functions">
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning",
                JOptionPane.WARNING_MESSAGE);
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

        trainersList = new javax.swing.JComboBox<>();
        trainersLabel = new javax.swing.JLabel();
        editButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        selectButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Trainer");
        setResizable(false);

        trainersLabel.setText("Trainer");

        editButton.setText("Edit...");

        addButton.setText("Add...");

        exitButton.setText("Exit");

        selectButton.setText("Select Trainer");

        deleteButton.setText("Delete Trainer");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(trainersLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(trainersList, 0, 244, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(trainersLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trainersList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton)
                    .addComponent(addButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton)
                    .addComponent(selectButton)
                    .addComponent(deleteButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton selectButton;
    private javax.swing.JLabel trainersLabel;
    private javax.swing.JComboBox<Trainer> trainersList;
    // End of variables declaration//GEN-END:variables
}
