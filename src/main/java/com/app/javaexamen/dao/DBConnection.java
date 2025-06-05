package com.app.javaexamen.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestionnaire de connexion à la base de données MySQL
 */
public class DBConnection {
    
    // Configuration de la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_colis_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private static Connection connection;

    static {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Établir la connexion
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            System.out.println("✅ Connexion à la base de données établie avec succès!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL non trouvé: " + e.getMessage());
            throw new RuntimeException("Driver MySQL non trouvé", e);
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données: " + e.getMessage());
            System.err.println("🔧 Vérifiez que:");
            System.err.println("   - MySQL est démarré");
            System.err.println("   - La base de données 'gestion_colis_db' existe");
            System.err.println("   - Les paramètres de connexion sont corrects");
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }

    /**
     * Retourne la connexion à la base de données
     * @return Connection active
     */
    public static Connection getConnection() {
        try {
            // Vérifier si la connexion est toujours active
            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 Reconnexion à la base de données...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification de connexion: " + e.getMessage());
            throw new RuntimeException("Problème de connexion à la base de données", e);
        }
        
        return connection;
    }
    
    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔐 Connexion à la base de données fermée");
            } catch (SQLException e) {
                System.err.println("⚠️ Erreur lors de la fermeture de connexion: " + e.getMessage());
            }
        }
    }
    
    /**
     * Teste la connexion à la base de données
     * @return true si la connexion fonctionne
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
