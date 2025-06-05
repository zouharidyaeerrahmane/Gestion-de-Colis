# 🚀 DÉMARRAGE RAPIDE - Application Gestion de Colis

## ⏱️ Démarrage en 5 minutes

### 1️⃣ **Prérequis (2 min)**
```bash
# Vérifier Java 21+
java --version

# Vérifier Maven
mvn --version

# Vérifier MySQL
mysql --version
```

### 2️⃣ **Base de Données (2 min)**
```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script (depuis le dossier du projet)
mysql -u root -p < database_setup.sql

# Vérifier
mysql -u root -p -e "USE gestion_colis_db; SELECT * FROM vue_statistiques;"
```

### 3️⃣ **Lancer l'Application (1 min)**
```bash
# Aller dans le dossier du projet
cd javaExamen

# Lancer directement
mvn javafx:run
```

## 🎯 **Test Rapide de Fonctionnement**

### ✅ **Checklist 30 secondes :**
1. **Interface s'ouvre** → Onglets "Livreurs" et "Colis" visibles
2. **Données de test** → Tables pré-remplies avec exemples
3. **Ajouter un livreur** → Remplir nom/prénom/téléphone → "Ajouter"
4. **Créer un colis** → Remplir destinataire/adresse → "Ajouter" 
5. **Export CSV** → Clic "Exporter livraisons du jour" → Barre de progression

## 🔧 **Résolution de Problèmes Courants**

### ❌ **"Connexion base de données échouée"**
```bash
# Démarrer MySQL
sudo systemctl start mysql
# Ou sur Windows : net start mysql

# Recréer la base
mysql -u root -p < database_setup.sql
```

### ❌ **"Module javafx.controls not found"**
```bash
# Utiliser Maven (gère automatiquement JavaFX)
mvn javafx:run
```

### ❌ **"Port 3306 déjà utilisé"**
Modifier dans `DBConnection.java` :
```java
private static final String DB_URL = "jdbc:mysql://localhost:3307/gestion_colis_db";
```

## 📋 **Données de Test Disponibles**

L'application démarre avec :
- **5 livreurs** prêts à utiliser
- **10 colis** avec différents statuts
- **Livraisons du jour** pour tester l'export CSV

## 🎮 **Scénario de Démonstration**

### **Demo 2 minutes :**
1. **Onglet Livreurs** → Montrer la liste, ajouter "Nouveau Testeur"
2. **Onglet Colis** → Créer colis, assigner au nouveau livreur
3. **Marquer livré** → Sélectionner colis → "Marquer livré"
4. **Export CSV** → "Exporter livraisons" → Voir progression
5. **Ouvrir CSV** → Vérifier contenu exporté

## 🏆 **Points Forts à Mettre en Avant**

✅ **Architecture Clean** : 3 couches bien séparées  
✅ **Interface Moderne** : JavaFX avec FXML  
✅ **Thread Safe** : Export CSV non-bloquant  
✅ **Base Robuste** : MySQL avec contraintes  
✅ **Validation Complète** : Gestion d'erreurs métier  
✅ **Documentation** : README + tests + quick start  

---

## 🎯 **Commandes Essentielles**

```bash
# Compilation
mvn clean compile

# Lancement
mvn javafx:run

# Tests base de données
mysql -u root -p gestion_colis_db -e "SHOW TABLES;"

# Voir logs application
tail -f logs/application.log
```

**🎊 Votre application de gestion de colis est prête à impressionner !**