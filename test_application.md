# Plan de Test - Application Gestion de Colis

## 🧪 Tests Fonctionnels

### Test 1: Gestion des Livreurs

#### Test 1.1: Ajout d'un livreur
- [ ] Ouvrir l'onglet "Gestion des Livreurs"
- [ ] Remplir les champs : Nom="Test", Prénom="Utilisateur", Téléphone="0123456789"
- [ ] Cliquer sur "Ajouter"
- [ ] ✅ **Résultat attendu :** Livreur ajouté dans la table, message de succès

#### Test 1.2: Modification d'un livreur
- [ ] Sélectionner un livreur dans la table
- [ ] Double-cliquer pour remplir les champs
- [ ] Modifier le téléphone
- [ ] Cliquer sur "Modifier"
- [ ] ✅ **Résultat attendu :** Modifications sauvegardées

#### Test 1.3: Suppression d'un livreur
- [ ] Sélectionner un livreur
- [ ] Cliquer sur "Supprimer"
- [ ] Confirmer la suppression
- [ ] ✅ **Résultat attendu :** Livreur supprimé, confirmation demandée

#### Test 1.4: Recherche de livreurs
- [ ] Saisir un nom dans la zone de recherche
- [ ] Cliquer sur "Rechercher"
- [ ] ✅ **Résultat attendu :** Filtrage des résultats

### Test 2: Gestion des Colis

#### Test 2.1: Ajout d'un colis
- [ ] Ouvrir l'onglet "Gestion des Colis"
- [ ] Remplir les champs obligatoires
- [ ] Cliquer sur "Ajouter"
- [ ] ✅ **Résultat attendu :** Colis ajouté avec statut "En attente"

#### Test 2.2: Assignation d'un colis
- [ ] Sélectionner un colis non assigné
- [ ] Choisir un livreur dans la ComboBox
- [ ] Cliquer sur "Assigner"
- [ ] ✅ **Résultat attendu :** Colis assigné au livreur

#### Test 2.3: Marquage comme livré
- [ ] Sélectionner un colis assigné
- [ ] Cliquer sur "Marquer livré"
- [ ] Confirmer l'action
- [ ] ✅ **Résultat attendu :** Statut changé en "Livré"

#### Test 2.4: Filtres
- [ ] Tester le filtre par statut ("Tous", "En attente", "Livrés")
- [ ] Tester le filtre par livreur
- [ ] ✅ **Résultat attendu :** Table filtrée correctement

### Test 3: Export CSV

#### Test 3.1: Export des livraisons du jour
- [ ] S'assurer qu'il y a des colis livrés aujourd'hui
- [ ] Cliquer sur "Exporter les livraisons du jour"
- [ ] Choisir un emplacement de sauvegarde
- [ ] Observer la barre de progression
- [ ] ✅ **Résultat attendu :** Fichier CSV créé avec les données

#### Test 3.2: Vérification du contenu CSV
- [ ] Ouvrir le fichier CSV généré
- [ ] Vérifier l'en-tête : "ID;Destinataire;Adresse;Date Envoi;Statut;Livreur"
- [ ] Vérifier les données
- [ ] ✅ **Résultat attendu :** Données correctes et formatées

## 🔧 Tests Techniques

### Test 4: Validation des Données

#### Test 4.1: Champs obligatoires
- [ ] Essayer d'ajouter un livreur sans nom
- [ ] Essayer d'ajouter un colis sans destinataire
- [ ] ✅ **Résultat attendu :** Messages d'erreur appropriés

#### Test 4.2: Unicité téléphone livreur
- [ ] Essayer d'ajouter deux livreurs avec le même téléphone
- [ ] ✅ **Résultat attendu :** Erreur d'unicité

#### Test 4.3: Validation des dates
- [ ] Essayer d'ajouter un colis avec une date future
- [ ] ✅ **Résultat attendu :** Erreur de validation

### Test 5: Base de Données

#### Test 5.1: Connexion
- [ ] Démarrer l'application
- [ ] Vérifier les messages de connexion dans la console
- [ ] ✅ **Résultat attendu :** "✅ Connexion à la base de données établie avec succès!"

#### Test 5.2: Persistance
- [ ] Ajouter des données
- [ ] Fermer et relancer l'application
- [ ] ✅ **Résultat attendu :** Données conservées

### Test 6: Interface Utilisateur

#### Test 6.1: Navigation
- [ ] Basculer entre les onglets
- [ ] Redimensionner la fenêtre
- [ ] ✅ **Résultat attendu :** Interface responsive

#### Test 6.2: Sélection dans les tables
- [ ] Cliquer sur une ligne
- [ ] Observer l'activation/désactivation des boutons
- [ ] ✅ **Résultat attendu :** Boutons activés selon le contexte

## 🚨 Tests d'Erreur

### Test 7: Gestion des Erreurs

#### Test 7.1: Base de données déconnectée
- [ ] Arrêter MySQL pendant l'utilisation
- [ ] Essayer une opération
- [ ] ✅ **Résultat attendu :** Message d'erreur clair

#### Test 7.2: Fichier CSV inaccessible
- [ ] Essayer d'exporter vers un dossier protégé
- [ ] ✅ **Résultat attendu :** Erreur d'écriture gérée

#### Test 7.3: Suppression avec contraintes
- [ ] Essayer de supprimer un livreur avec des colis assignés
- [ ] ✅ **Résultat attendu :** Gestion appropriée des contraintes

## 📊 Checklist de Validation Finale

### Fonctionnalités Requises
- [ ] ✅ Gestion complète des livreurs (CRUD)
- [ ] ✅ Gestion complète des colis (CRUD)
- [ ] ✅ Assignation colis ↔ livreur
- [ ] ✅ Marquage de livraison
- [ ] ✅ Export CSV avec thread JavaFX
- [ ] ✅ Interface JavaFX fonctionnelle

### Architecture
- [ ] ✅ Couche DAO implémentée
- [ ] ✅ Couche Service avec logique métier
- [ ] ✅ Couche Présentation (Controllers)
- [ ] ✅ Entités métier définies
- [ ] ✅ Packages organisés correctement

### Technologies
- [ ] ✅ Java 21+
- [ ] ✅ JavaFX pour l'interface
- [ ] ✅ JDBC avec MySQL
- [ ] ✅ Architecture 3 couches respectée

### Base de Données
- [ ] ✅ Tables créées (livreurs, colis)
- [ ] ✅ Relations définies
- [ ] ✅ Données de test insérées
- [ ] ✅ Contraintes d'intégrité

### Interface Utilisateur
- [ ] ✅ Fichiers FXML créés
- [ ] ✅ Contrôleurs liés
- [ ] ✅ Validation des saisies
- [ ] ✅ Messages d'erreur/succès

### Export & Threads
- [ ] ✅ Export CSV fonctionnel
- [ ] ✅ Thread JavaFX (Task)
- [ ] ✅ Barre de progression
- [ ] ✅ Gestion des erreurs d'export

## 🎯 Critères de Réussite

Pour que l'application soit considérée comme réussie :

1. **Fonctionnel** : Tous les tests de la section "Tests Fonctionnels" passent
2. **Technique** : Architecture respectée, code propre, gestion d'erreurs
3. **Interface** : Ergonomique, responsive, messages clairs
4. **Performance** : Export CSV rapide, pas de blocage UI
5. **Robustesse** : Gestion des cas d'erreur, validation des données

---

**Note** : Ce plan de test couvre tous les aspects requis par l'examen Java POO & JAVA 2025.