package com.app.javaexamen.dao;

import com.app.javaexamen.entities.Colis;
import com.app.javaexamen.entities.Livreur;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO pour la gestion des colis
 */
public interface ColisDAO extends Dao<Colis, Integer> {
    /**
     * Récupère tous les colis d'un livreur spécifique
     */
    List<Colis> findByLivreur(Livreur livreur) throws SQLException;
    
    /**
     * Récupère tous les colis d'un livreur par son ID
     */
    List<Colis> findByLivreurId(int livreurId) throws SQLException;
    
    /**
     * Récupère tous les colis livrés
     */
    List<Colis> findLivredColis() throws SQLException;
    
    /**
     * Récupère tous les colis non livrés
     */
    List<Colis> findNonLivredColis() throws SQLException;
    
    /**
     * Récupère tous les colis livrés à une date spécifique
     */
    List<Colis> findColisLivresParDate(LocalDate date) throws SQLException;
    
    /**
     * Marque un colis comme livré
     */
    void marquerCommeLivre(int colisId) throws SQLException;
    
    /**
     * Assigne un colis à un livreur
     */
    void assignerLivreur(int colisId, int livreurId) throws SQLException;
}