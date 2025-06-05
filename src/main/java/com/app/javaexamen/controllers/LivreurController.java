package com.app.javaexamen.controllers;

import com.app.javaexamen.entities.Livreur;
import com.app.javaexamen.services.LivreurService;
import com.app.javaexamen.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la gestion des livreurs
 */
public class LivreurController implements Initializable {

    @FXML
    private TableView<Livreur> livreurTable;

    @FXML
    private TableColumn<Livreur, Integer> idColumn;

    @FXML
    private TableColumn<Livreur, String> nomColumn;

    @FXML
    private TableColumn<Livreur, String> prenomColumn;

    @FXML
    private TableColumn<Livreur, String> telephoneColumn;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField searchNomField;

    @FXML
    private TextField searchPrenomField;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    @FXML
    private Button rechercherButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label countLabel;

    private LivreurService livreurService;
    private ObservableList<Livreur> livreurData;
    private Livreur selectedLivreur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            livreurService = new LivreurService();
            livreurData = FXCollections.observableArrayList();
            
            setupTable();
            setupButtons();
            setupEventHandlers();
            loadLivreurs();
        } catch (Exception e) {
            AlertUtil.showError("Erreur d'initialisation", 
                              "Impossible d'initialiser la gestion des livreurs", e);
        }
    }

    /**
     * Configure la table des livreurs
     */
    private void setupTable() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Lier les données à la table
        livreurTable.setItems(livreurData);

        // Configuration de la sélection
        livreurTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Configure les boutons
     */
    private void setupButtons() {
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
    }

    /**
     * Configure les gestionnaires d'événements
     */
    private void setupEventHandlers() {
        // Sélection dans la table
        livreurTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> onLivreurSelected(newValue)
        );

        // Boutons
        ajouterButton.setOnAction(event -> ajouterLivreur());
        modifierButton.setOnAction(event -> modifierLivreur());
        supprimerButton.setOnAction(event -> supprimerLivreur());
        rechercherButton.setOnAction(event -> rechercherLivreurs());
        clearButton.setOnAction(event -> clearFields());

        // Double-clic sur la table
        livreurTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedLivreur != null) {
                fillFieldsWithSelected();
            }
        });
    }

    /**
     * Charge tous les livreurs depuis la base de données
     */
    public void loadLivreurs() {
        try {
            List<Livreur> livreurs = livreurService.obtenirTousLesLivreurs();
            livreurData.clear();
            livreurData.addAll(livreurs);
            updateCountLabel();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de chargement", 
                              "Impossible de charger la liste des livreurs", e);
        }
    }

    /**
     * Gère la sélection d'un livreur dans la table
     */
    private void onLivreurSelected(Livreur livreur) {
        selectedLivreur = livreur;
        boolean hasSelection = livreur != null;
        
        modifierButton.setDisable(!hasSelection);
        supprimerButton.setDisable(!hasSelection);
    }

    /**
     * Ajoute un nouveau livreur
     */
    @FXML
    private void ajouterLivreur() {
        try {
            if (!validateFields()) {
                return;
            }

            Livreur nouveauLivreur = new Livreur(
                nomField.getText().trim(),
                prenomField.getText().trim(),
                telephoneField.getText().trim()
            );

            livreurService.ajouterLivreur(nouveauLivreur);
            loadLivreurs();
            clearFields();
            
            AlertUtil.showSuccess("Livreur ajouté", 
                                "Le livreur " + nouveauLivreur.getFullName() + " a été ajouté avec succès.");

        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Données invalides", e.getMessage());
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de base de données", 
                              "Impossible d'ajouter le livreur", e);
        }
    }

    /**
     * Modifie le livreur sélectionné
     */
    @FXML
    private void modifierLivreur() {
        if (selectedLivreur == null) {
            AlertUtil.showWarning("Aucune sélection", "Veuillez sélectionner un livreur à modifier.");
            return;
        }

        try {
            if (!validateFields()) {
                return;
            }

            selectedLivreur.setNom(nomField.getText().trim());
            selectedLivreur.setPrenom(prenomField.getText().trim());
            selectedLivreur.setTelephone(telephoneField.getText().trim());

            livreurService.modifierLivreur(selectedLivreur);
            loadLivreurs();
            clearFields();
            
            AlertUtil.showSuccess("Livreur modifié", 
                                "Le livreur a été modifié avec succès.");

        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Données invalides", e.getMessage());
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de base de données", 
                              "Impossible de modifier le livreur", e);
        }
    }

    /**
     * Supprime le livreur sélectionné
     */
    @FXML
    private void supprimerLivreur() {
        if (selectedLivreur == null) {
            AlertUtil.showWarning("Aucune sélection", "Veuillez sélectionner un livreur à supprimer.");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmation(
            "Confirmer la suppression",
            "Êtes-vous sûr de vouloir supprimer le livreur " + selectedLivreur.getFullName() + " ?\n" +
            "Cette action est irréversible."
        );

        if (confirmed) {
            try {
                livreurService.supprimerLivreur(selectedLivreur.getId());
                loadLivreurs();
                clearFields();
                
                AlertUtil.showSuccess("Livreur supprimé", 
                                    "Le livreur a été supprimé avec succès.");

            } catch (SQLException e) {
                AlertUtil.showError("Erreur de base de données", 
                                  "Impossible de supprimer le livreur", e);
            } catch (IllegalArgumentException e) {
                AlertUtil.showWarning("Suppression impossible", e.getMessage());
            }
        }
    }

    /**
     * Recherche des livreurs par nom et prénom
     */
    @FXML
    private void rechercherLivreurs() {
        try {
            String nom = searchNomField.getText();
            String prenom = searchPrenomField.getText();
            
            List<Livreur> resultats = livreurService.rechercherLivreurs(nom, prenom);
            livreurData.clear();
            livreurData.addAll(resultats);
            updateCountLabel();

        } catch (SQLException e) {
            AlertUtil.showError("Erreur de recherche", 
                              "Impossible de rechercher les livreurs", e);
        }
    }

    /**
     * Vide tous les champs de saisie
     */
    @FXML
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        telephoneField.clear();
        searchNomField.clear();
        searchPrenomField.clear();
        selectedLivreur = null;
        livreurTable.getSelectionModel().clearSelection();
    }

    /**
     * Remplit les champs avec les données du livreur sélectionné
     */
    private void fillFieldsWithSelected() {
        if (selectedLivreur != null) {
            nomField.setText(selectedLivreur.getNom());
            prenomField.setText(selectedLivreur.getPrenom());
            telephoneField.setText(selectedLivreur.getTelephone());
        }
    }

    /**
     * Valide les champs de saisie
     */
    private boolean validateFields() {
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Champ requis", "Le nom est obligatoire.");
            nomField.requestFocus();
            return false;
        }

        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Champ requis", "Le prénom est obligatoire.");
            prenomField.requestFocus();
            return false;
        }

        if (telephoneField.getText() == null || telephoneField.getText().trim().isEmpty()) {
            AlertUtil.showWarning("Champ requis", "Le numéro de téléphone est obligatoire.");
            telephoneField.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Met à jour le label de comptage
     */
    private void updateCountLabel() {
        int count = livreurData.size();
        countLabel.setText(count + " livreur(s)");
    }

    /**
     * Retourne la liste des livreurs pour utilisation par d'autres contrôleurs
     */
    public ObservableList<Livreur> getLivreurData() {
        return livreurData;
    }

    /**
     * Retourne le livreur sélectionné
     */
    public Livreur getSelectedLivreur() {
        return selectedLivreur;
    }
}