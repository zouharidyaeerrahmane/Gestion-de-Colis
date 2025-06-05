package com.app.javaexamen.controllers;

import com.app.javaexamen.entities.Colis;
import com.app.javaexamen.services.ColisService;
import com.app.javaexamen.util.AlertUtil;
import com.app.javaexamen.util.CSVExportService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur principal de l'application
 */
public class MainController implements Initializable {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab livreurTab;

    @FXML
    private Tab colisTab;

    @FXML
    private Button exportButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    private ColisService colisService;
    private LivreurController livreurController;
    private ColisController colisController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colisService = new ColisService();
            initializeTabs();
            setupExportButton();
            setupProgressBar();
        } catch (Exception e) {
            AlertUtil.showError("Erreur d'initialisation", 
                              "Impossible d'initialiser l'application", e);
        }
    }

    /**
     * Initialise les onglets avec leurs contrôleurs respectifs
     */
    private void initializeTabs() throws IOException {
        // Charger l'onglet des livreurs
        FXMLLoader livreurLoader = new FXMLLoader(getClass().getResource("/com/app/javaexamen/livreur.fxml"));
        livreurTab.setContent(livreurLoader.load());
        livreurController = livreurLoader.getController();

        // Charger l'onglet des colis
        FXMLLoader colisLoader = new FXMLLoader(getClass().getResource("/com/app/javaexamen/colis.fxml"));
        colisTab.setContent(colisLoader.load());
        colisController = colisLoader.getController();
        
        // Passer la référence du contrôleur livreur au contrôleur colis
        if (colisController != null && livreurController != null) {
            colisController.setLivreurController(livreurController);
        }
    }

    /**
     * Configure le bouton d'exportation
     */
    private void setupExportButton() {
        exportButton.setOnAction(event -> exportLivraisonsDuJour());
    }

    /**
     * Configure la barre de progression
     */
    private void setupProgressBar() {
        progressBar.setVisible(false);
        statusLabel.setText("Prêt");
    }

    /**
     * Exporte les livraisons du jour en CSV
     */
    @FXML
    private void exportLivraisonsDuJour() {
        try {
            // Récupérer les colis livrés aujourd'hui
            List<Colis> colisLivresAujourdhui = colisService.obtenirColisLivresAujourdhui();
            
            if (colisLivresAujourdhui.isEmpty()) {
                AlertUtil.showInfo("Aucune livraison", 
                                 "Aucun colis n'a été livré aujourd'hui (" + LocalDate.now() + ")");
                return;
            }

            // Dialogue de sélection du fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder l'export CSV");
            fileChooser.setInitialFileName(CSVExportService.generateDefaultFileName("livraisons"));
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv")
            );

            Stage stage = (Stage) exportButton.getScene().getWindow();
            File fichierDestination = fileChooser.showSaveDialog(stage);

            if (fichierDestination != null) {
                exporterVersCSV(colisLivresAujourdhui, fichierDestination.toPath());
            }

        } catch (SQLException e) {
            AlertUtil.showError("Erreur de base de données", 
                              "Impossible de récupérer les livraisons du jour", e);
        } catch (Exception e) {
            AlertUtil.showError("Erreur", 
                              "Une erreur inattendue s'est produite", e);
        }
    }

    /**
     * Lance l'exportation CSV dans un thread séparé
     */
    private void exporterVersCSV(List<Colis> colis, java.nio.file.Path fichierDestination) {
        // Créer la tâche d'exportation
        Task<String> exportTask = CSVExportService.createExportTask(colis, fichierDestination);

        // Configurer l'interface utilisateur
        progressBar.setVisible(true);
        progressBar.progressProperty().bind(exportTask.progressProperty());
        statusLabel.textProperty().bind(exportTask.messageProperty());
        exportButton.setDisable(true);

        // Gérer la fin de la tâche
        exportTask.setOnSucceeded(e -> {
            progressBar.setVisible(false);
            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Prêt");
            exportButton.setDisable(false);
            
            AlertUtil.showSuccess("Export réussi", exportTask.getValue());
        });

        exportTask.setOnFailed(e -> {
            progressBar.setVisible(false);
            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Erreur lors de l'export");
            exportButton.setDisable(false);
            
            Throwable exception = exportTask.getException();
            AlertUtil.showError("Erreur d'exportation", 
                              "L'exportation a échoué", 
                              exception instanceof Exception ? (Exception) exception : new Exception(exception));
        });

        exportTask.setOnCancelled(e -> {
            progressBar.setVisible(false);
            progressBar.progressProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.setText("Export annulé");
            exportButton.setDisable(false);
        });

        // Lancer la tâche dans un thread séparé
        Thread exportThread = new Thread(exportTask);
        exportThread.setDaemon(true);
        exportThread.start();
    }

    /**
     * Actualise les données dans tous les onglets
     */
    public void refreshAllData() {
        if (livreurController != null) {
            livreurController.loadLivreurs();
        }
        if (colisController != null) {
            colisController.loadColis();
        }
    }

    /**
     * Getter pour le contrôleur des livreurs
     */
    public LivreurController getLivreurController() {
        return livreurController;
    }

    /**
     * Getter pour le contrôleur des colis
     */
    public ColisController getColisController() {
        return colisController;
    }
}