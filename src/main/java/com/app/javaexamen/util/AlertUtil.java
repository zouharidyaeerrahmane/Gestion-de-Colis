package com.app.javaexamen.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

/**
 * Utilitaire pour la gestion des alertes et dialogues JavaFX
 */
public class AlertUtil {
    
    /**
     * Affiche une alerte d'information
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une alerte d'erreur
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une alerte d'avertissement
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("Attention");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une demande de confirmation
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirmation");
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Affiche un dialogue de saisie de texte
     */
    public static Optional<String> showTextInput(String title, String headerText, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        
        return dialog.showAndWait();
    }
    
    /**
     * Affiche une alerte de succès
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une erreur avec détails d'exception
     */
    public static void showError(String title, String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Erreur");
        
        String fullMessage = message;
        if (e != null && e.getMessage() != null) {
            fullMessage += "\n\nDétails: " + e.getMessage();
        }
        
        alert.setContentText(fullMessage);
        alert.showAndWait();
    }
}