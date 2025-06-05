package com.app.javaexamen.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entité représentant un colis
 */
public class Colis {
    private int id;
    private String destinataire;
    private String adresse;
    private LocalDate dateEnvoi;
    private boolean livre;
    private Livreur livreur;

    // Constructeurs
    public Colis() {}

    public Colis(String destinataire, String adresse, LocalDate dateEnvoi) {
        this.destinataire = destinataire;
        this.adresse = adresse;
        this.dateEnvoi = dateEnvoi;
        this.livre = false;
    }

    public Colis(int id, String destinataire, String adresse, LocalDate dateEnvoi, boolean livre, Livreur livreur) {
        this.id = id;
        this.destinataire = destinataire;
        this.adresse = adresse;
        this.dateEnvoi = dateEnvoi;
        this.livre = livre;
        this.livreur = livreur;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDate dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public boolean isLivre() {
        return livre;
    }

    public void setLivre(boolean livre) {
        this.livre = livre;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    // Méthodes utilitaires
    @Override
    public String toString() {
        return "Colis #" + id + " - " + destinataire + " (" + (livre ? "Livré" : "En attente") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colis colis = (Colis) o;
        return id == colis.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getStatutLivraison() {
        return livre ? "Livré" : "En attente";
    }

    public String getNomLivreur() {
        return livreur != null ? livreur.getFullName() : "Non assigné";
    }
}