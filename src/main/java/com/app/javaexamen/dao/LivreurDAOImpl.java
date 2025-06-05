package com.app.javaexamen.dao;

import com.app.javaexamen.entities.Livreur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de LivreurDAO pour la gestion des livreurs en base de données
 */
public class LivreurDAOImpl implements LivreurDAO {
    
    private Connection connection;
    
    public LivreurDAOImpl() {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public void create(Livreur livreur) throws SQLException {
        String sql = "INSERT INTO livreurs (nom, prenom, telephone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livreur.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM livreurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Livreur> findAll() throws SQLException {
        List<Livreur> livreurs = new ArrayList<>();
        String sql = "SELECT * FROM livreurs ORDER BY nom, prenom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livreur livreur = new Livreur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone")
                );
                livreurs.add(livreur);
            }
        }
        return livreurs;
    }

    @Override
    public Livreur findByID(Integer id) throws SQLException {
        String sql = "SELECT * FROM livreurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livreur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void update(Livreur livreur) throws SQLException {
        String sql = "UPDATE livreurs SET nom = ?, prenom = ?, telephone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            stmt.setInt(4, livreur.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Livreur> findByNomPrenom(String nom, String prenom) throws SQLException {
        List<Livreur> livreurs = new ArrayList<>();
        String sql = "SELECT * FROM livreurs WHERE nom LIKE ? AND prenom LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            stmt.setString(2, "%" + prenom + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livreur livreur = new Livreur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone")
                    );
                    livreurs.add(livreur);
                }
            }
        }
        return livreurs;
    }

    @Override
    public Livreur findByTelephone(String telephone) throws SQLException {
        String sql = "SELECT * FROM livreurs WHERE telephone = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livreur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Livreur> findAvailableDeliverers() throws SQLException {
        // Pour cet exemple, on considère que tous les livreurs sont disponibles
        return findAll();
    }
}