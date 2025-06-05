package com.app.javaexamen.services;

import com.app.javaexamen.dao.LivreurDAO;
import com.app.javaexamen.dao.LivreurDAOImpl;
import com.app.javaexamen.entities.Livreur;
import java.sql.SQLException;
import java.util.List;

/**
 * Service pour la gestion de la logique métier des livreurs
 */
public class LivreurService {
    
    private LivreurDAO livreurDAO;
    
    public LivreurService() {
        this.livreurDAO = new LivreurDAOImpl();
    }
    
    /**
     * Ajoute un nouveau livreur après validation
     */
    public void ajouterLivreur(Livreur livreur) throws SQLException, IllegalArgumentException {
        validateLivreur(livreur);
        
        // Vérifier l'unicité du numéro de téléphone
        Livreur existant = livreurDAO.findByTelephone(livreur.getTelephone());
        if (existant != null) {
            throw new IllegalArgumentException("Un livreur avec ce numéro de téléphone existe déjà.");
        }
        
        livreurDAO.create(livreur);
    }
    
    /**
     * Modifie un livreur existant
     */
    public void modifierLivreur(Livreur livreur) throws SQLException, IllegalArgumentException {
        validateLivreur(livreur);
        
        // Vérifier si le livreur existe
        Livreur existant = livreurDAO.findByID(livreur.getId());
        if (existant == null) {
            throw new IllegalArgumentException("Livreur introuvable.");
        }
        
        // Vérifier l'unicité du téléphone (sauf pour lui-même)
        Livreur livreurAvecTelephone = livreurDAO.findByTelephone(livreur.getTelephone());
        if (livreurAvecTelephone != null && livreurAvecTelephone.getId() != livreur.getId()) {
            throw new IllegalArgumentException("Un autre livreur avec ce numéro de téléphone existe déjà.");
        }
        
        livreurDAO.update(livreur);
    }
    
    /**
     * Supprime un livreur
     */
    public void supprimerLivreur(int livreurId) throws SQLException, IllegalArgumentException {
        Livreur existant = livreurDAO.findByID(livreurId);
        if (existant == null) {
            throw new IllegalArgumentException("Livreur introuvable.");
        }
        
        // TODO: Vérifier s'il a des colis assignés et gérer la suppression
        livreurDAO.deleteById(livreurId);
    }
    
    /**
     * Récupère tous les livreurs
     */
    public List<Livreur> obtenirTousLesLivreurs() throws SQLException {
        return livreurDAO.findAll();
    }
    
    /**
     * Récupère un livreur par son ID
     */
    public Livreur obtenirLivreurParId(int id) throws SQLException {
        return livreurDAO.findByID(id);
    }
    
    /**
     * Récupère les livreurs disponibles
     */
    public List<Livreur> obtenirLivreursDisponibles() throws SQLException {
        return livreurDAO.findAvailableDeliverers();
    }
    
    /**
     * Recherche des livreurs par nom et prénom
     */
    public List<Livreur> rechercherLivreurs(String nom, String prenom) throws SQLException {
        if ((nom == null || nom.trim().isEmpty()) && (prenom == null || prenom.trim().isEmpty())) {
            return obtenirTousLesLivreurs();
        }
        
        String nomRecherche = nom != null ? nom.trim() : "";
        String prenomRecherche = prenom != null ? prenom.trim() : "";
        
        return livreurDAO.findByNomPrenom(nomRecherche, prenomRecherche);
    }
    
    /**
     * Valide les données d'un livreur
     */
    private void validateLivreur(Livreur livreur) throws IllegalArgumentException {
        if (livreur == null) {
            throw new IllegalArgumentException("Le livreur ne peut pas être null.");
        }
        
        if (livreur.getNom() == null || livreur.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du livreur est obligatoire.");
        }
        
        if (livreur.getPrenom() == null || livreur.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du livreur est obligatoire.");
        }
        
        if (livreur.getTelephone() == null || livreur.getTelephone().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire.");
        }
        
        // Validation basique du format de téléphone
        String telephone = livreur.getTelephone().trim();
        if (!telephone.matches("^[0-9+\\-\\s()]{8,15}$")) {
            throw new IllegalArgumentException("Le format du numéro de téléphone n'est pas valide.");
        }
        
        // Nettoyer les données
        livreur.setNom(livreur.getNom().trim());
        livreur.setPrenom(livreur.getPrenom().trim());
        livreur.setTelephone(telephone);
    }
}