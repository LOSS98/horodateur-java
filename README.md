# Application de Réservation de Places de Parking

L’application de réservation de places de parking fonctionne comme un horodateur. Elle permet au client qui l’ouvre d’entrer l’immatriculation et le type de son véhicule afin de réserver une place de parking. Il peut choisir un étage s’il reste des places disponibles, sinon le système lui en attribue un autre. Le système réserve ensuite la place pour le client et lui indique que sa place est réservée. S’il n’y a pas de place disponible, un message s’affiche indiquant qu’il ne peut pas réserver de place.

Le client peut indiquer pendant combien de temps il souhaite réserver la place à partir du moment où il effectue sa réservation. Une indication est également indiquée sur la page pour informer l’utilisateur du nombre de places disponibles dans le parking.

Le type de voiture permet de déterminer le type de place et le tarif de stationnement à l’heure. Il existe des places classiques, handicapées, familles, de recharge électrique et 2 roues. La place de parking a un numéro, un étage et une indication sur sa validité (est-elle en travaux ?) ainsi que sur sa disponibilité (est-elle déjà réservée ?).

Il y a également des coefficients pour la tarification en fonction du calendrier : est-on en semaine, en weekend, en jour férié ou en vacances scolaires ?

Il existe également un accès administrateur sur lequel on peut voir l’historique des réservations. Cet accès est disponible en cliquant sur le bouton approprié et en rentrant un code.

Pour une amélioration du projet, on pourrait accéder à l’administration via un compte administrateur mais la gestion des authentifications n’est pas notre priorité actuellement.

## 4.2. Conception

### 4.2.1. Utilisateur

Pour implémenter notre application, nous avons choisi d'utiliser le logiciel SceneBuilder pour concevoir l'interface utilisateur, complétée par notre propre code Java. Une partie de l'interface de l'application est présentée ci-dessous. Elle intègre les fonctionnalités prévues ainsi que les optimisations de design élaborées à l'aide de Figma.

#### Page d’accueil
- **Type de place sélectionné indisponible**

#### Saisie des informations de la réservation
Sur cette page, le DateTimePicker que nous souhaitions utiliser pour enregistrer la date et l'heure de fin de réservation est manquant. Nous avons constaté qu'il n'est pas disponible dans le logiciel que nous utilisons actuellement. Il sera donc intégré ultérieurement, lors de la phase finale de conception.

#### Récapitulatif de réservation
Lorsque la place est réservée, l’utilisateur reçoit un message récapitulatif de sa demande ainsi que la place attribuée.

### 4.2.2. Administrateurs

#### Modification d’une place de parking

#### Ajouter une place de parking

#### Consultation de l'historique de réservation
- Télécharger l'historique de réservation avec date/heure de début et fin.

## Guide d’utilisation

### Pour l’utilisateur :

#### Sélection du parking et de la place :
Commencez par sélectionner le parking et le type de place souhaité. Si aucune place n'est disponible, modifiez les critères de votre recherche en ajustant vos préférences.

#### Réservation de la place :
Si une place est disponible, vous serez redirigé vers la page d’enregistrement de la réservation.
Renseignez les informations suivantes :
- La plaque d’immatriculation de votre véhicule.
- La date et l’heure de fin de réservation (elles doivent être supérieures à la date et l’heure actuelles).
- L’étage de préférence, si applicable.

#### Récapitulatif et paiement :
Après validation, vous serez dirigé vers une page récapitulative de votre réservation.
Vous y trouverez les détails de la réservation ainsi que le montant total à payer.
Conservez ce reçu pour vos besoins.

### Pour l’administrateur

#### Authentification
Cliquez sur le bouton situé en haut à droite de la page d’accueil pour accéder à la page de connexion. Saisissez vos identifiants pour vous connecter.

#### Gestion et administration
Après authentification, plusieurs fonctionnalités vous seront accessibles :
- **Gérer et afficher les parkings** : Consultez les détails des parkings existants et ajoutez de nouveaux parkings si nécessaire.
- **Gérer et afficher les places de stationnement** : Visualisez et modifiez l’état des places disponibles selon chaque parking.
- **Gérer les utilisateurs** : Ajoutez, modifiez ou supprimez des comptes d’utilisateurs et d’administrateurs.
- **Générer des rapports** : Produisez des statistiques sur les revenus et les taux d’occupation.
- **Consulter l’historique des réservations** : Accédez aux détails des réservations passées pour un suivi précis.
