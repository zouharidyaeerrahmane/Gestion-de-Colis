# Application de Gestion de Colis 

## 📋 Description

Application de bureau développée en JavaFX pour la gestion de colis livrés par des livreurs à domicile.
Cette application respecte une architecture 3 couches (DAO, Service, Présentation) et utilise MySQL comme base de données.

## 🏗️ Architecture du Projet

```
src/main/java/com/app/javaexamen/
├── Main.java                 # Point d'entrée de l'application
├── controllers/              # Couche Présentation
│   ├── MainController.java   # Contrôleur principal
│   ├── LivreurController.java# Gestion des livreurs
│   └── ColisController.java  # Gestion des colis
├── entities/                 # Entités métier
│   ├── Livreur.java         # Entité Livreur
│   └── Colis.java           # Entité Colis
├── dao/                     # Couche d'accès aux données
│   ├── Dao.java             # Interface générique DAO
│   ├── DBConnection.java    # Gestionnaire de connexion
│   ├── LivreurDAO.java      # Interface DAO Livreur
│   ├── LivreurDAOImpl.java  # Implémentation DAO Livreur
│   ├── ColisDAO.java        # Interface DAO Colis
│   └── ColisDAOImpl.java    # Implémentation DAO Colis
├── services/                # Couche logique métier
│   ├── LivreurService.java  # Service Livreur
│   └── ColisService.java    # Service Colis
└── util/                    # Classes utilitaires
    ├── CSVExportService.java # Export CSV avec threads
    └── AlertUtil.java       # Utilitaires d'interface
```

## 🛠️ Technologies Utilisées

- **Java 21** - Langage de programmation
- **JavaFX 21** - Interface utilisateur
- **MySQL 8.0+** - Base de données
- **JDBC** - Connecteur base de données
- **Maven** - Gestionnaire de dépendances

## 📦 Fonctionnalités

### Gestion des Livreurs
- ✅ Ajouter un nouveau livreur (nom, prénom, téléphone)
- ✅ Modifier les informations d'un livreur
- ✅ Supprimer un livreur
- ✅ Afficher la liste des livreurs disponibles
- ✅ Rechercher des livreurs par nom/prénom

### Gestion des Colis
- ✅ Ajouter un nouveau colis (destinataire, adresse, date d'envoi)
- ✅ Associer un colis à un livreur
- ✅ Voir les colis d'un livreur
- ✅ Marquer un colis comme livré
- ✅ Supprimer un colis
- ✅ Filtrer les colis par statut et livreur

### Exportation
- ✅ Bouton "Exporter les livraisons du jour" → fichier CSV
- ✅ Opération effectuée dans un Thread JavaFX avec barre de progression
- ✅ Sauvegarde avec dialogue de sélection de fichier

## 🚀 Installation et Configuration

### 1. Prérequis
- Java JDK 21 ou supérieur
- MySQL 8.0 ou supérieur
- Maven 3.6 ou supérieur

### 2. Configuration de la Base de Données

1. **Démarrer MySQL** et se connecter en tant que root
2. **Exécuter le script de création** :
   ```sql
   mysql -u root -p < database_setup.sql
   ```

3. **Vérifier la création** :
   ```sql
   USE gestion_colis_db;
   SHOW TABLES;
   SELECT * FROM vue_statistiques;
   ```

### 3. Configuration de l'Application

Si nécessaire, modifier les paramètres de connexion dans `DBConnection.java` :
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_colis_db";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";
```

### 4. Compilation et Exécution

```bash
# Compilation
mvn clean compile

# Exécution
mvn javafx:run
```

## 📱 Guide d'Utilisation

### Interface Principale
L'application s'ouvre avec deux onglets principaux :
- **Gestion des Livreurs** : CRUD complet des livreurs
- **Gestion des Colis** : CRUD complet des colis avec assignation

### Workflow Typique
1. **Ajouter des livreurs** dans l'onglet "Gestion des Livreurs"
2. **Créer des colis** dans l'onglet "Gestion des Colis"
3. **Assigner des colis** aux livreurs
4. **Marquer comme livrés** les colis terminés
5. **Exporter les livraisons** du jour en CSV

### Export CSV
- Cliquer sur "Exporter les livraisons du jour" dans la barre d'outils
- Choisir l'emplacement de sauvegarde
- Suivre la progression dans la barre de statut

## 🗃️ Structure de la Base de Données

### Table `livreurs`
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `nom` (VARCHAR(100), NOT NULL)
- `prenom` (VARCHAR(100), NOT NULL)
- `telephone` (VARCHAR(20), NOT NULL, UNIQUE)

### Table `colis`
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `destinataire` (VARCHAR(200), NOT NULL)
- `adresse` (TEXT, NOT NULL)
- `date_envoi` (DATE, NOT NULL)
- `livre` (BOOLEAN, DEFAULT FALSE)
- `livreur_id` (INT, FOREIGN KEY → livreurs.id)

## 🔧 Dépannage

### Problèmes de Connexion MySQL
```
❌ Erreur de connexion à la base de données
```
**Solutions :**
1. Vérifier que MySQL est démarré
2. Vérifier que la base `gestion_colis_db` existe
3. Vérifier les paramètres de connexion (user/password)
4. Exécuter le script `database_setup.sql`

### Problèmes JavaFX
```
❌ Module javafx.controls not found
```
**Solution :** Vérifier la version de Java et les dépendances Maven

### Erreurs d'Export CSV
**Vérifications :**
- Droits d'écriture sur le dossier de destination
- Espace disque suffisant
- Chemin de fichier valide

## 📊 Données de Test

L'application est livrée avec des données de test :
- 5 livreurs prédéfinis
- 10 colis d'exemple avec différents statuts
- Livraisons du jour pour tester l'export

## 🎯 Critères d'Évaluation Respectés

- ✅ **Architecture 3 couches** : DAO, Service, Controller séparés
- ✅ **JavaFX** : Interface utilisateur complète avec FXML
- ✅ **JDBC + MySQL** : Persistance des données
- ✅ **Gestion des livreurs** : CRUD complet
- ✅ **Gestion des colis** : CRUD + assignation + livraison
- ✅ **Export CSV** : Thread JavaFX avec Task
- ✅ **Validation des données** : Contrôles métier
- ✅ **Gestion d'erreurs** : Try-catch et alertes utilisateur

## 👨‍💻 Développement

### Commandes Utiles
```bash
# Nettoyer le projet
mvn clean

# Compiler
mvn compile

# Lancer l'application
mvn javafx:run

# Créer un package
mvn package
```

### Structure des Packages
- `entities` : Modèles de données métier
- `dao` : Accès aux données avec interfaces
- `services` : Logique métier et validation
- `controllers` : Gestion de l'interface JavaFX
- `util` : Classes utilitaires (CSV, alertes)

---

## 📝 Notes

Cette application a été développée dans le cadre de l'examen Java POO & JAVA du 2ème semestre 2024/2025 de l'École Normale Supérieure de l'Enseignement Technique.