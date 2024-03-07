Architecture de l'Application
-----------------------------

L'architecture de l'application Game of Life est basée sur le framework Spring Boot, qui offre une structure modulaire et extensible. L'application suit le modèle MVC (Modèle-Vue-Contrôleur) pour une gestion efficace des requêtes et des réponses.

Composants Principaux
---------------------

### 1\. Contrôleurs

Les contrôleurs sont responsables de la gestion des requêtes HTTP et WebSocket. Ils interceptent les requêtes provenant des clients, effectuent les opérations nécessaires et retournent les réponses appropriées.

### 2\. Services

Les services contiennent la logique métier de l'application. Ils traitent les requêtes des contrôleurs, effectuent les opérations métier et interagissent avec les repositories pour accéder aux données.

### 3\. Modèles

Les modèles représentent les données manipulées par l'application. Ils sont responsables du stockage et de la manipulation des informations nécessaires au fonctionnement de l'application.

### 4\. Repositories

Les repositories fournissent un accès aux données persistantes. Ils encapsulent les opérations de lecture et d'écriture sur la base de données, facilitant ainsi la communication entre l'application et le stockage de données.

Fonctionnalités Clés
--------------------

### 1\. Authentification et Gestion des Utilisateurs

L'application permet aux utilisateurs de s'authentifier et de s'inscrire. Elle utilise des sessions HTTP pour gérer l'état de l'utilisateur et garantir l'accès sécurisé aux fonctionnalités.

### 2\. Jeu de la Vie en Temps Réel

Le jeu de la vie est implémenté en utilisant WebSocket pour permettre une interaction en temps réel entre les utilisateurs. Les règles du jeu peuvent être modifiées dynamiquement par les utilisateurs via le chat.

### 3\. Communication en Temps Réel avec WebSocket

WebSocket est utilisé pour la communication bidirectionnelle entre le serveur et les clients. Cela permet d'envoyer des mises à jour en temps réel, telles que les changements dans le jeu de la vie et les messages du chat.

### 4\. Gestion de la Base de Données

L'application utilise une base de données MySQL pour stocker les informations utilisateur et les données du jeu. Les repositories assurent l'accès sécurisé aux données et garantissent la cohérence des opérations de lecture et d'écriture.

Configuration et Déploiement
----------------------------

### 1\. Configuration de l'Application

La configuration de l'application est définie dans le fichier `application.properties`. Ce fichier contient des paramètres tels que l'URL de la base de données, les informations d'authentification et les options de journalisation.

### 2\. Déploiement de l'Application

Pour déployer l'application, il suffit d'exécuter la classe principale `GameOfLifeApplication`. L'application peut être déployée sur n'importe quel serveur compatible avec Java, en veillant à ce que toutes les dépendances soient correctement gérées.