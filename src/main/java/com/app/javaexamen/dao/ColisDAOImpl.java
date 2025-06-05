package com.app.javaexamen.dao;

import com.app.javaexamen.entities.Colis;
import com.app.javaexamen.entities.Livreur;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de ColisDAO pour la gestion des colis en base de données
 */
public class ColisDAOImpl implements ColisDAO {
    
    private Connection connection;
    private LivreurDAO livreurDAO;
    
    public ColisDAOImpl() {
        this.connection = DBConnection.getConnection();
        this.livreurDAO = new LivreurDAOImpl();
    }

    @Override
    public void create(Colis colis) throws SQLException {
        String sql = "INSERT INTO colis (destinataire, adresse, date_envoi, livre, livreur_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, colis.getDestinataire());
            stmt.setString(2, colis.getAdresse());
            stmt.setDate(3, Date.valueOf(colis.getDateEnvoi()));
            stmt.setBoolean(4, colis.isLivre());
            
            if (colis.getLivreur() != null) {
                stmt.setInt(5, colis.getLivreur().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        colis.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM colis WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Colis> findAll() throws SQLException {
        List<Colis> colisList = new ArrayList<>();
        String sql = """
            SELECT c.*, l.nom, l.prenom, l.telephone 
            FROM colis c 
            LEFT JOIN livreurs l ON c.livreur_id = l.id 
            ORDER BY c.date_envoi DESC
        """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Colis colis = createColisFromResultSet(rs);
                colisList.add(colis);
            }
        }
        return colisList;
    }

    @Override
    public Colis findByID(Integer id) throws SQLException {
        String sql = """
            SELECT c.*, l.nom, l.prenom, l.telephone 
            FROM colis c 
            LEFT JOIN livreurs l ON c.livreur_id = l.id 
            WHERE c.id = ?
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createColisFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Colis colis) throws SQLException {
        String sql = "UPDATE colis SET destinataire = ?, adresse = ?, date_envoi = ?, livre = ?, livreur_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, colis.getDestinataire());
            stmt.setString(2, colis.getAdresse());
            stmt.setDate(3, Date.valueOf(colis.getDateEnvoi()));
            stmt.setBoolean(4, colis.isLivre());
            
            if (colis.getLivreur() != null) {
                stmt.setInt(5, colis.getLivreur().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setInt(6, colis.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Colis> findByLivreur(Livreur livreur) throws SQLException {
        return findByLivreurId(livreur.getId());
    }

    @Override
    public List<Colis> findByLivreurId(int livreurId) throws SQLException {
        List<Colis> colisList = new ArrayList<>();
        String sql = """
            SELECT c.*, l.nom, l.prenom, l.telephone 
            FROM colis c 
            LEFT JOIN livreurs l ON c.livreur_id = l.id 
            WHERE c.livreur_id = ? 
            ORDER BY c.date_envoi DESC
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, livreurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Colis colis = createColisFromResultSet(rs);
                    colisList.add(colis);
                }
            }
        }
        return colisList;
    }

    @Override
    public List<Colis> findLivredColis() throws SQLException {
        List<Colis> colisList = new ArrayList<>();
        String sql = """
            SELECT c.*, l.nom, l.prenom, l.telephone 
            FROM colis c 
            LEFT JOIN livreurs l ON c.livreur_id = l.id 
            WHERE c.livre = true 
            ORDER BY c.date_envoi DESC
        """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Colis colis = createColisFromResultSet(rs);
                colisList.add(colis);
            }
        }
        return colisList;
    }

    @Override
    public List<Colis> findNonLivredColis() throws SQLException {
        List<Colis> colisList = new ArrayList<>();
        String sql = """
            SELECT c.*, l.nom, l.prenom, l.telephone 
            FROM colis c 
            LEFT JOIN livreurs l ON c.livreur_id = l.id 
            WHERE c.livre = false 
            ORDER BY c.date_envoi DESC
        """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Colis colis = createColisFromResultSet(rs);
                colisList.add(colis);
            }
        }
        return colisList;
    }

    @Override
    public List<Colis> findColisLivresParDate(LocalDate date) throws SQLException {
        List<Colis> colisList = new ArrayList<>();
        String sql = """
            SELECT c.*, l.nom, l.prenom, l.telephone 
            FROM colis c 
            LEFT JOIN livreurs l ON c.livreur_id = l.id 
            WHERE c.livre = true AND DATE(c.date_envoi) = ? 
            ORDER BY c.date_envoi DESC
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Colis colis = createColisFromResultSet(rs);
                    colisList.add(colis);
                }
            }
        }
        return colisList;
    }

    @Override
    public void marquerCommeLivre(int colisId) throws SQLException {
        String sql = "UPDATE colis SET livre = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, colisId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void assignerLivreur(int colisId, int livreurId) throws SQLException {
        String sql = "UPDATE colis SET livreur_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, livreurId);
            stmt.setInt(2, colisId);
            stmt.executeUpdate();
        }
    }

    /**
     * Méthode utilitaire pour créer un objet Colis à partir d'un ResultSet
     */
    private Colis createColisFromResultSet(ResultSet rs) throws SQLException {
        Colis colis = new Colis();
        colis.setId(rs.getInt("id"));
        colis.setDestinataire(rs.getString("destinataire"));
        colis.setAdresse(rs.getString("adresse"));
        colis.setDateEnvoi(rs.getDate("date_envoi").toLocalDate());
        colis.setLivre(rs.getBoolean("livre"));
        
        // Vérifier si un livreur est associé
        if (rs.getInt("livreur_id") != 0) {
            Livreur livreur = new Livreur(
                rs.getInt("livreur_id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("telephone")
            );
            colis.setLivreur(livreur);
        }
        
        return colis;
    }
}