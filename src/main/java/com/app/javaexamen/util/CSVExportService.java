package com.app.javaexamen.util;

import com.app.javaexamen.entities.Colis;
import javafx.concurrent.Task;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service d'exportation CSV utilisant les tâches JavaFX
 */
public class CSVExportService {
    
    private static final String CSV_SEPARATOR = ";";
    private static final String CSV_HEADER = "ID;Destinataire;Adresse;Date Envoi;Statut;Livreur";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Crée une tâche JavaFX pour exporter les colis en CSV
     */
    public static Task<String> createExportTask(List<Colis> colis, Path fichierDestination) {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("Début de l'exportation...");
                updateProgress(0, 100);
                
                try (FileWriter writer = new FileWriter(fichierDestination.toFile())) {
                    // Écrire l'en-tête
                    writer.write(CSV_HEADER);
                    writer.write(System.lineSeparator());
                    
                    updateMessage("Écriture des données...");
                    updateProgress(10, 100);
                    
                    // Écrire les données
                    int totalColis = colis.size();
                    int currentIndex = 0;
                    
                    for (Colis c : colis) {
                        if (isCancelled()) {
                            break;
                        }
                        
                        writer.write(formatColisForCSV(c));
                        writer.write(System.lineSeparator());
                        
                        currentIndex++;
                        double progress = 10 + (currentIndex * 80.0 / totalColis);
                        updateProgress(progress, 100);
                        updateMessage(String.format("Exportation en cours... (%d/%d)", currentIndex, totalColis));
                        
                        // Simulation d'un traitement (pour voir l'animation)
                        Thread.sleep(50);
                    }
                    
                    updateProgress(100, 100);
                    updateMessage("Exportation terminée!");
                    
                    return String.format("Exportation réussie: %d colis exportés vers %s", 
                                       totalColis, fichierDestination.getFileName());
                                       
                } catch (IOException e) {
                    updateMessage("Erreur lors de l'exportation: " + e.getMessage());
                    throw new RuntimeException("Erreur lors de l'écriture du fichier CSV: " + e.getMessage(), e);
                }
            }
        };
    }
    
    /**
     * Crée une tâche pour exporter les livraisons du jour
     */
    public static Task<String> createTodayDeliveriesExportTask(List<Colis> colisLivresAujourdhui) {
        String fileName = String.format("livraisons_%s.csv", 
                         LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Path defaultPath = Path.of(System.getProperty("user.home"), "Desktop", fileName);
        
        return createExportTask(colisLivresAujourdhui, defaultPath);
    }
    
    /**
     * Formate un colis pour l'export CSV
     */
    private static String formatColisForCSV(Colis colis) {
        StringBuilder sb = new StringBuilder();
        
        // ID
        sb.append(colis.getId()).append(CSV_SEPARATOR);
        
        // Destinataire (échapper les caractères spéciaux)
        sb.append(escapeCSVField(colis.getDestinataire())).append(CSV_SEPARATOR);
        
        // Adresse
        sb.append(escapeCSVField(colis.getAdresse())).append(CSV_SEPARATOR);
        
        // Date d'envoi
        sb.append(colis.getDateEnvoi().format(DATE_FORMATTER)).append(CSV_SEPARATOR);
        
        // Statut
        sb.append(colis.isLivre() ? "Livré" : "En attente").append(CSV_SEPARATOR);
        
        // Livreur
        sb.append(escapeCSVField(colis.getNomLivreur()));
        
        return sb.toString();
    }
    
    /**
     * Échappe les caractères spéciaux pour CSV
     */
    private static String escapeCSVField(String field) {
        if (field == null) {
            return "";
        }
        
        String escaped = field.replace("\"", "\"\"");
        
        if (escaped.contains(CSV_SEPARATOR) || escaped.contains("\"") || 
            escaped.contains("\n") || escaped.contains("\r")) {
            escaped = "\"" + escaped + "\"";
        }
        
        return escaped;
    }
    
    /**
     * Génère un nom de fichier par défaut
     */
    public static String generateDefaultFileName(String prefix) {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return String.format("%s_%s.csv", prefix, timestamp);
    }
    
    /**
     * Obtient le chemin par défaut pour les exports
     */
    public static Path getDefaultExportPath(String fileName) {
        return Path.of(System.getProperty("user.home"), "Desktop", fileName);
    }
}