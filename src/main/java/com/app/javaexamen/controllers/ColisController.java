package com.app.javaexamen.controllers;

import com.app.javaexamen.entities.Colis;
import com.app.javaexamen.entities.Livreur;
import com.app.javaexamen.services.ColisService;
import com.app.javaexamen.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la gestion des colis
 */
public class ColisController implements Initializable {

    @FXML
    private TableView<Colis> colisTable;

    @FXML
    private TableColumn<Colis, Integer> idColumn;

    @FXML
    private TableColumn<Colis, String> destinataireColumn;

    @FXML
    private TableColumn<Colis, String> adresseColumn;

    @FXML
    private TableColumn<Colis, LocalDate> dateEnvoiColumn;

    @FXML
    private TableColumn<Colis, String> statutColumn;

    @FXML
    private TableColumn<Colis, String> livreurColumn;

    @FXML
    private TextField destinataireField;

    @FXML
    private TextArea adresseField;

    @FXML
    private DatePicker dateEnvoiPicker;

    @FXML
    private ComboBox<Livreur> livreurComboBox;

    @FXML
    private CheckBox livreCheckBox;

    @FXML
    private ComboBox<String> filtreStatutComboBox;

    @FXML
    private ComboBox<Livreur> filtreLivreurComboBox;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    @FXML
    private Button assignerButton;

    @FXML
    private Button marquerLivreButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label countLabel;

    private ColisService colisService;
    private ObservableList<Colis> colisData;
    private ObservableList<Colis> allColisData; // Pour les filtres
    private Colis selectedColis;
    private LivreurController livreurController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colisService = new ColisService();
            colisData = FXCollections.observableArrayList();
            allColisData = FXCollections.observableArrayList();
            
            setupTable();
            setupButtons();
            setupComboBoxes();
            setupEventHandlers();
            loadColis();
        } catch (Exception e) {
            AlertUtil.showError("Erreur d'initialisation", 
                              "Impossible d'initialiser la gestion des colis", e);
        }
    }

    /**
     * Configure la table des colis
     */
    private void setupTable() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        destinataireColumn.setCellValueFactory(new PropertyValueFactory<>("destinataire"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        dateEnvoiColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnvoi"));
        
        // Colonnes avec données calculées
        statutColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatutLivraison()));
        livreurColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomLivreur()));

        // Lier les données à la table
        colisTable.setItems(colisData);

        // Configuration de la sélection
        colisTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Configure les boutons
     */
    private void setupButtons() {
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
        assignerButton.setDisable(true);
        marquerLivreButton.setDisable(true);
    }

    /**
     * Configure les ComboBox
     */
    private void setupComboBoxes() {
        // Filtre par statut
        filtreStatutComboBox.getItems().addAll("Tous", "En attente", "Livrés");
        filtreStatutComboBox.setValue("Tous");

        // Date par défaut
        dateEnvoiPicker.setValue(LocalDate.now());

        // Configurar para mostrar el nombre completo del livreur
        livreurComboBox.setConverter(new javafx.util.StringConverter<Livreur>() {
            @Override
            public String toString(Livreur livreur) {
                return livreur != null ? livreur.getFullName() : "";
            }

            @Override
            public Livreur fromString(String string) {
                return null; // Non utilisé
            }
        });

        filtreLivreurComboBox.setConverter(new javafx.util.StringConverter<Livreur>() {
            @Override
            public String toString(Livreur livreur) {
                return livreur != null ? livreur.getFullName() : "Tous";
            }

            @Override
            public Livreur fromString(String string) {
                return null; // Non utilisé
            }
        });
    }

    /**
     * Configure les gestionnaires d'événements
     */
    private void setupEventHandlers() {
        // Sélection dans la table
        colisTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> onColisSelected(newValue)
        );

        // Boutons
        ajouterButton.setOnAction(event -> ajouterColis());
        modifierButton.setOnAction(event -> modifierColis());
        supprimerButton.setOnAction(event -> supprimerColis());
        assignerButton.setOnAction(event -> assignerColis());
        marquerLivreButton.setOnAction(event -> marquerCommeLivre());
        clearButton.setOnAction(event -> clearFields());

        // Filtres
        filtreStatutComboBox.setOnAction(event -> applyFilters());
        filtreLivreurComboBox.setOnAction(event -> applyFilters());

        // Double-clic sur la table
        colisTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedColis != null) {
                fillFieldsWithSelected();
            }
        });
    }

    /**
     * Charge tous les colis depuis la base de données
     */
    public void loadColis() {
        try {
            List<Colis> colis = colisService.obtenirTousLesColis();
            allColisData.clear();
            allColisData.addAll(colis);
            applyFilters();
            updateCountLabel();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de chargement", 
                              "Impossible de charger la liste des colis", e);
        }
    }

    /**
     * Applique les filtres sélectionnés
     */
    private void applyFilters() {
        colisData.clear();
        
        String statutFilter = filtreStatutComboBox.getValue();
        Livreur livreurFilter = filtreLivreurComboBox.getValue();
        
        for (Colis colis : allColisData) {
            boolean passesFilter = true;
            
            // Filtre par statut
            if (statutFilter != null && !statutFilter.equals("Tous")) {
                if (statutFilter.equals("En attente") && colis.isLivre()) {
                    passesFilter = false;
                } else if (statutFilter.equals("Livrés") && !colis.isLivre()) {
                    passesFilter = false;
                }
            }
            
            // Filtre par livreur
            if (livreurFilter != null && colis.getLivreur() != null) {
                if (!colis.getLivreur().equals(livreurFilter)) {
                    passesFilter = false;
                }
            }
            
            if (passesFilter) {
                colisData.add(colis);
            }
        }
        
        updateCountLabel();
    }

    /**
     * Gère la sélection d'un colis dans la table
     */
    private void onColisSelected(Colis colis) {
        selectedColis = colis;
        boolean hasSelection = colis != null;
        
        modifierButton.setDisable(!hasSelection);
        supprimerButton.setDisable(!hasSelection);
        assignerButton.setDisable(!hasSelection || colis.isLivre());
        marquerLivreButton.setDisable(!hasSelection || colis.isLivre() || colis.getLivreur() == null);
    }

    /**
     * Ajoute un nouveau colis
     */
    @FXML
    private void ajouterColis() {
        try {
            if (!validateFields()) {
                return;
            }

            Colis nouveauColis = new Colis(
                destinataireField.getText().trim(),
                adresseField.getText().trim(),
                dateEnvoiPicker.getValue()
            );

            if (livreurComboBox.getValue() != null) {
                nouveauColis.setLivreur(livreurComboBox.getValue());
            }

            colisService.ajouterColis(nouveauColis);
            loadColis();
            clearFields();
            
            AlertUtil.showSuccess("Colis ajouté", 
                                "Le colis pour " + nouveauColis.getDestinataire() + " a été ajouté avec succès.");

        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Données invalides", e.getMessage());
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de base de données", 
                              "Impossible d'ajouter le colis", e);
        }
    }

    /**
     * Modifie le colis sélectionné
     */
    @FXML
    private void modifierColis() {
        if (selectedColis == null) {
            AlertUtil.showWarning("Aucune sélection", "Veuillez sélectionner un colis à modifier.");
            return;
        }

        try {
            if (!validateFields()) {
                return;
            }

            selectedColis.setDestinataire(destinataireField.getText().trim());
            selectedColis.setAdresse(adresseField.getText().trim());
            selectedColis.setDateEnvoi(dateEnvoiPicker.getValue());
            selectedColis.setLivre(livreCheckBox.isSelected());
            selectedColis.setLivreur(livreurComboBox.getValue());

            colisService.modifierColis(selectedColis);
            loadColis();
            clearFields();
            
            AlertUtil.showSuccess("Colis modifié", 
                                "Le colis a été modifié avec succès.");

        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Données invalides", e.getMessage());
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de base de données", 
                              "Impossible de modifier le colis", e);
        }
    }

    /**
     * Supprime le colis sélectionné
     */
    @FXML
    private void supprimerColis() {
        if (selectedColis == null) {
            AlertUtil.showWarning("Aucune sélection", "Veuillez sélectionner un colis à supprimer.");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmation(
            "Confirmer la suppression",
            "Êtes-vous sûr de vouloir supprimer le colis #" + selectedColis.getId() + 
            " pour " + selectedColis.getDestinataire() + " ?\n" +
            "Cette action est irréversible."
        );

        if (confirmed) {
            try {
                colisService.supprimerColis(selectedColis.getId());
                loadColis();
                clearFields();
                
                AlertUtil.showSuccess("Colis supprimé", 
                                    "Le colis a été supprimé avec succès.");

            } catch (SQLException e) {
                AlertUtil.showError("Erreur de base de données", 
                                  "Impossible de supprimer le colis", e);
            }
        }
    }

    /**
     * Assigne un livreur au colis sélectionné
     */
    @FXML
    private void assignerColis() {
        if (selectedColis == null) {
            AlertUtil.showWarning("Aucune sélection", "Veuillez sélectionner un colis.");
            return;
        }

        if (livreurComboBox.getValue() == null) {
            AlertUtil.showWarning("Livreur requis", "Veuillez sélectionner un livreur.");
            return;
        }

        try {
            colisService.assignerColis(selectedColis.getId(), livreurComboBox.getValue().getId());
            loadColis();
            
            AlertUtil.showSuccess("Assignation réussie", 
                                "Le colis a été assigné à " + livreurComboBox.getValue().getFullName() + ".");

        } catch (SQLException e) {
            AlertUtil.showError("Erreur de base de données", 
                              "Impossible d'assigner le colis", e);
        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Assignation impossible", e.getMessage());
        }
    }

    /**
     * Marque le colis sélectionné comme livré
     */
    @FXML
    private void marquerCommeLivre() {
        if (selectedColis == null) {
            AlertUtil.showWarning("Aucune sélection", "Veuillez sélectionner un colis.");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmation(
            "Confirmer la livraison",
            "Confirmer que le colis #" + selectedColis.getId() + 
            " pour " + selectedColis.getDestinataire() + " a été livré ?"
        );

        if (confirmed) {
            try {
                colisService.marquerCommeLivre(selectedColis.getId());
                loadColis();
                clearFields();
                
                AlertUtil.showSuccess("Colis livré", 
                                    "Le colis a été marqué comme livré.");

            } catch (SQLException e) {
                AlertUtil.showError("Erreur de base de données", 
                                  "Impossible de marquer le colis comme livré", e);
            } catch (IllegalArgumentException e) {
                AlertUtil.showWarning("Marquage impossible", e.getMessage());
            }
        }
    }

    /**
     * Vide tous les champs de saisie
     */
    @FXML
    private void clearFields() {
        destinataireField.clear();
        adresseField.clear();
        dateEnvoiPicker.setValue(LocalDate.now());
        livreurComboBox.setValue(null);
        livreCheckBox.setSelected(false);
        selectedColis = null;
        colisTable.getSelectionModel().clearSelection();
    }

    /**
     * Remplit les champs avec les données du colis sélectionné
     */
    private void fillFieldsWithSelected() {
        if (selectedColis != null) {
            destinataireField.setText(selectedColis.getDestinataire());
            adresseField.setText(selectedColis.getAdresse());
            dateEnvoiPicker.setValue(selectedColis.getDateEnvoi());
            livreurComboBox.setValue(selectedColis.getLivreur());
            livreCheckBox.setSelected(selectedColis.isLivre());
        }
    }

    /**
     * Valide les champs de saisie
     */
    private boolean validateFields() {
        if (destinataireField.getText() == null || destinataireField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Champ requis", "Le nom du destinataire est obligatoire.");
            destinataireField.requestFocus();
            return false;
        }

        if (adresseField.getText() == null || adresseField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Champ requis", "L'adresse de livraison est obligatoire.");
            adresseField.requestFocus();
            return false;
        }

        if (dateEnvoiPicker.getValue() == null) {
            AlertUtil.showWarning("Champ requis", "La date d'envoi est obligatoire.");
            dateEnvoiPicker.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Met à jour le label de comptage
     */
    private void updateCountLabel() {
        int count = colisData.size();
        countLabel.setText(count + " colis");
    }

    /**
     * Définit la référence au contrôleur des livreurs
     */
    public void setLivreurController(LivreurController livreurController) {
        this.livreurController = livreurController;
        if (livreurController != null) {
            // Mettre à jour les ComboBox avec les livreurs
            updateLivreurComboBoxes();
        }
    }

    /**
     * Met à jour les ComboBox de livreurs
     */
    public void updateLivreurComboBoxes() {
        if (livreurController != null) {
            ObservableList<Livreur> livreurs = livreurController.getLivreurData();
            
            livreurComboBox.setItems(livreurs);
            
            // Pour le filtre, ajouter "Tous" au début
            ObservableList<Livreur> livreursAvecTous = FXCollections.observableArrayList();
            livreursAvecTous.add(null); // Représente "Tous"
            livreursAvecTous.addAll(livreurs);
            filtreLivreurComboBox.setItems(livreursAvecTous);
            filtreLivreurComboBox.setValue(null);
        }
    }

    /**
     * Retourne la liste des colis pour utilisation par d'autres contrôleurs
     */
    public ObservableList<Colis> getColisData() {
        return colisData;
    }
}