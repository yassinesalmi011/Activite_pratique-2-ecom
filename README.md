# Projet E-Commerce Microservices (Spring Cloud)

Ce projet est une application e-commerce basée sur une architecture microservices avec Spring Boot et Spring Cloud.

## Architecture

Le projet est composé des services suivants :

1.  **Customer Service** : Gestion des clients (JPA, H2, Spring Data REST).
2.  **Inventory Service** : Gestion des produits (JPA, H2, Spring Data REST).
3.  **Gateway Service** : Point d'entrée unique (Spring Cloud Gateway), routage dynamique.
4.  **Discovery Service** : Annuaire des services (Netflix Eureka).
5.  **Billing Service** : Gestion des factures (OpenFeign pour communiquer avec Customer et Inventory).
6.  **Config Service** : Gestion centralisée de la configuration.

## Prérequis

*   Java 21
*   Maven

## Démarrage

Il est important de démarrer les services dans l'ordre suivant :

1.  **Config Service** (Port 9999)
2.  **Discovery Service** (Port 8761)
3.  **Gateway Service** (Port 8888)
4.  **Customer Service** (Port 8081)
5.  **Inventory Service** (Port 8082)
6.  **Billing Service** (Port 8083)

## Fonctionnalités et Captures d'écran

### 1. Eureka Discovery Service
Tableau de bord montrant tous les services enregistrés.
![Eureka Dashboard](captures/eureka_dashboard.png)
*(Placez votre capture d'écran ici : http://localhost:8761)*

### 2. Configuration Centralisée
Le `billing-service` récupère sa configuration depuis le `config-service`.
Exemple de réponse du Config Server :
![Config Server Response](captures/config_server.png)
*(Placez votre capture d'écran ici : http://localhost:9999/billing-service/default)*

### 3. Billing Service (OpenFeign & RestController)
Le service de facturation récupère les infos client et produits via des appels REST (Feign Client).
La réponse JSON complète de la facture (Question 7 & 8) :
![Billing Json](captures/billing_response.png)
*(Placez votre capture d'écran ici : http://localhost:8083/bills/1)*

### 4. Base de données H2
Accès à la console H2 pour vérifier les données.
![H2 Console](captures/h2_console.png)
*(Placez votre capture d'écran ici : http://localhost:8083/h2-console)*

## Auteurs
*   **Yassine** - *Initial work*
