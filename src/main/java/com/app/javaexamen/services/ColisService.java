package com.app.javaexamen.services;

import com.app.javaexamen.dao.ColisDAO;
import com.app.javaexamen.dao.ColisDAOImpl;
import com.app.javaexamen.dao.LivreurDAO;
import com.app.javaexamen.dao.LivreurDAOImpl;
import com.app.javaexamen.entities.Colis;
import com.app.javaexamen.entities.Livreur;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion de la logique métier des colis
 */
public class ColisService {
    
    private ColisDAO colisDAO;
    private LivreurDAO livreurDAO;
    
    public ColisService() {
        this.colisDAO = new ColisDAOImpl();
        this.livreurDAO = new LivreurDAOImpl();
    }
    
    /**
     * Ajoute un nouveau colis après validation
     */
    public void ajouterColis(Colis colis) throws SQLException, IllegalArgumentException {
        validateColis(colis);
        
        // Par défaut, un nouveau colis n'est pas livré
        colis.setLivre(false);
        
        colisDAO.create(colis);
    }
    
    /**
     * Modifie un colis existant
     */
    public void modifierColis(Colis colis) throws SQLException, IllegalArgumentException {
        validateColis(colis);
        
        // Vérifier si le colis existe
        Colis existant = colisDAO.findByID(colis.getId());
        if (existant == null) {
            throw new IllegalArgumentException("Colis introuvable.");
        }
        
        colisDAO.update(colis);
    }
    
    /**
     * Supprime un colis
     */
    public void supprimerColis(int colisId) throws SQLException, IllegalArgumentException {
        Colis existant = colisDAO.findByID(colisId);
        if (existant == null) {
            throw new IllegalArgumentException("Colis introuvable.");
        }
        
        colisDAO.deleteById(colisId);
    }
    
    /**
     * Récupère tous les colis
     */
    public List<Colis> obtenirTousLesColis() throws SQLException {
        return colisDAO.findAll();
    }
    
    /**
     * Récupère un colis par son ID
     */
    public Colis obtenirColisParId(int id) throws SQLException {
        return colisDAO.findByID(id);
    }
    
    /**
     * Récupère tous les colis d'un livreur
     */
    public List<Colis> obtenirColisParLivreur(Livreur livreur) throws SQLException {
        if (livreur == null) {
            throw new IllegalArgumentException("Le livreur ne peut pas être null.");
        }
        return colisDAO.findByLivreur(livreur);
    }
    
    /**
     * Récupère tous les colis d'un livreur par son ID
     */
    public List<Colis> obtenirColisParLivreurId(int livreurId) throws SQLException {
        return colisDAO.findByLivreurId(livreurId);
    }
    
    /**
     * Récupère tous les colis livrés
     */
    public List<Colis> obtenirColisLivres() throws SQLException {
        return colisDAO.findLivredColis();
    }
    
    /**
     * Récupère tous les colis non livrés
     */
    public List<Colis> obtenirColisNonLivres() throws SQLException {
        return colisDAO.findNonLivredColis();
    }
    
    /**
     * Récupère tous les colis livrés aujourd'hui
     */
    public List<Colis> obtenirColisLivresAujourdhui() throws SQLException {
        return colisDAO.findColisLivresParDate(LocalDate.now());
    }
    
    /**
     * Récupère tous les colis livrés à une date spécifique
     */
    public List<Colis> obtenirColisLivresParDate(LocalDate date) throws SQLException {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null.");
        }
        return colisDAO.findColisLivresParDate(date);
    }
    
    /**
     * Assigne un colis à un livreur
     */
    public void assignerColis(int colisId, int livreurId) throws SQLException, IllegalArgumentException {
        // Vérifier que le colis existe
        Colis colis = colisDAO.findByID(colisId);
        if (colis == null) {
            throw new IllegalArgumentException("Colis introuvable.");
        }
        
        // Vérifier que le livreur existe
        Livreur livreur = livreurDAO.findByID(livreurId);
        if (livreur == null) {
            throw new IllegalArgumentException("Livreur introuvable.");
        }
        
        // Vérifier que le colis n'est pas déjà livré
        if (colis.isLivre()) {
            throw new IllegalArgumentException("Impossible d'assigner un colis déjà livré.");
        }
        
        colisDAO.assignerLivreur(colisId, livreurId);
    }
    
    /**
     * Marque un colis comme livré
     */
    public void marquerCommeLivre(int colisId) throws SQLException, IllegalArgumentException {
        // Vérifier que le colis existe
        Colis colis = colisDAO.findByID(colisId);
        if (colis == null) {
            throw new IllegalArgumentException("Colis introuvable.");
        }
        
        // Vérifier qu'un livreur est assigné
        if (colis.getLivreur() == null) {
            throw new IllegalArgumentException("Impossible de marquer comme livré un colis sans livreur assigné.");
        }
        
        // Vérifier que le colis n'est pas déjà livré
        if (colis.isLivre()) {
            throw new IllegalArgumentException("Ce colis est déjà marqué comme livré.");
        }
        
        colisDAO.marquerCommeLivre(colisId);
    }
    
    /**
     * Valide les données d'un colis
     */
    private void validateColis(Colis colis) throws IllegalArgumentException {
        if (colis == null) {
            throw new IllegalArgumentException("Le colis ne peut pas être null.");
        }
        
        if (colis.getDestinataire() == null || colis.getDestinataire().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du destinataire est obligatoire.");
        }
        
        if (colis.getAdresse() == null || colis.getAdresse().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse de livraison est obligatoire.");
        }
        
        if (colis.getDateEnvoi() == null) {
            throw new IllegalArgumentException("La date d'envoi est obligatoire.");
        }
        
        // Vérifier que la date d'envoi n'est pas dans le futur
        if (colis.getDateEnvoi().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date d'envoi ne peut pas être dans le futur.");
        }
        
        // Nettoyer les données
        colis.setDestinataire(colis.getDestinataire().trim());
        colis.setAdresse(colis.getAdresse().trim());
    }
}