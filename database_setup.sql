-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_colis_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestion_colis_db;

-- Création de la table des livreurs
CREATE TABLE IF NOT EXISTS livreurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table des colis
CREATE TABLE IF NOT EXISTS colis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    destinataire VARCHAR(200) NOT NULL,
    adresse TEXT NOT NULL,
    date_envoi DATE NOT NULL,
    livre BOOLEAN DEFAULT FALSE,
    livreur_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (livreur_id) REFERENCES livreurs(id) ON DELETE SET NULL
);
