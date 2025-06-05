package com.app.javaexamen.dao;

import com.app.javaexamen.entities.Livreur;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des livreurs
 */
public interface LivreurDAO extends Dao<Livreur, Integer> {
    /**
     * Recherche un livreur par son nom et prénom
     */
    List<Livreur> findByNomPrenom(String nom, String prenom) throws SQLException;
    
    /**
     * Recherche un livreur par son numéro de téléphone
     */
    Livreur findByTelephone(String telephone) throws SQLException;
    
    /**
     * Récupère tous les livreurs disponibles (actifs)
     */
    List<Livreur> findAvailableDeliverers() throws SQLException;
}