# Documentation de l'Application "Jeu de la Vie"

## Documentation Fonctionnelle

### Objectif
L'application "Jeu de la Vie" est une simulation basée sur les règles du célèbre automate cellulaire conçu par John Conway. Elle permet aux utilisateurs de configurer et de visualiser le comportement de l'automate sans intervention directe dans la simulation.

### Fonctionnalités

#### Authentification
Les utilisateurs peuvent se connecter à leur compte pour accéder à l'application.

#### Configuration du Jeu
Permet de définir les paramètres initiaux du jeu, notamment le nombre de cellules vivantes et mortes.

#### Simulation
Affichage de la simulation en temps réel selon les règles définies.

#### Sauvegarde des Paramètres
Les configurations du jeu peuvent être sauvegardées dans une base de données pour une utilisation ultérieure.

### Interface Utilisateur

#### Page de Connexion
Formulaire pour l'authentification des utilisateurs.

#### Tableau de Bord
Interface pour la configuration des paramètres du jeu et le lancement de la simulation.

#### Visualisation du Jeu
Grille représentant l'état actuel du jeu avec les cellules vivantes et mortes.

---

## Documentation Technique

### Architecture

#### Front-End
Interface utilisateur développée en JavaFX.

#### Back-End
Logique du jeu et gestion des données en Java.

#### Base de Données
MySQL pour stocker les informations utilisateur et les configurations du jeu.

### Sécurité

#### Authentification
Utilisation de JWT (JSON Web Tokens) pour la gestion des sessions.

#### Stockage des Mots de Passe
Hashage des mots de passe avec une fonction de hashage sécurisée (ex : bcrypt) et utilisation de sel.

### Dépendances

#### JavaFX
Pour la création de l'interface utilisateur.

#### JDBC
Pour la connexion à la base de données MySQL.

#### JUnit
Pour les tests unitaires.

### Installation et Configuration

1. **Installation des Dépendances**
   S'assurer que Java et MySQL sont installés.

2. **Configuration de la Base de Données**
   Créer la base de données et les tables nécessaires.

3. **Compilation et Exécution**
   Compiler le code et exécuter l'application.

### Tests

- Des tests unitaires seront écrits pour chaque composant majeur du système.
- Des tests d'intégration pour vérifier la cohérence entre les différents composants.

### Maintenance et Support

#### Mises à Jour
Planification régulière des mises à jour pour améliorer les fonctionnalités et corriger les bugs.

#### Support
Un système de suivi des problèmes via GitHub ou une plateforme similaire.
