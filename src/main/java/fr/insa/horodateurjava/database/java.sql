-- Table Place
CREATE TABLE Place (
                       numero INTEGER PRIMARY KEY,         -- Identifiant unique de la place
                       etage INTEGER NOT NULL,             -- Étage de la place
                       type VARCHAR(50) NOT NULL,             -- Type de la place
                       disponibilite BOOLEAN NOT NULL, -- Disponibilité (true = libre)
                       tarifHoraire FLOAT NOT NULL,    -- Tarif horaire
                       puissanceCharge FLOAT,   -- Puissance de charge en kW
                       enTravaux BOOLEAN NOT NULL      -- Place en travaux ou non
);

-- Table pour les réservations
CREATE TABLE Reservation (
                             numeroReservation INTEGER PRIMARY KEY,
                             immatriculation VARCHAR(50) NOT NULL, -- Numéro d'immatriculation du véhicule
                             numeroPlace INTEGER NOT NULL,             -- Clé étrangère vers Place
                             dateHeureDebut DATETIME NOT NULL,
                             dateHeureFin DATETIME NOT NULL,
                             FOREIGN KEY (numeroPlace) REFERENCES Place(numero)
);

-- Table pour les administrateurs
CREATE TABLE Administrateur (
                                idAdmin INTEGER PRIMARY KEY AUTOINCREMENT,
                                nom VARCHAR(100) NOT NULL,
                                prenom VARCHAR(100) NOT NULL,
                                email VARCHAR(255) UNIQUE NOT NULL,
                                motDePasse VARCHAR(255) NOT NULL -- Stocké de manière sécurisée (hash)
);

-- Table pour les rapports
CREATE TABLE Rapport (
                         idRapport INTEGER PRIMARY KEY AUTOINCREMENT,
                         tauxOccupation FLOAT NOT NULL,
                         heuresDePointe JSON, -- Ou format adapté pour stocker plusieurs dates
                         dateGeneration DATETIME NOT NULL,
                         idAdmin INTEGER NOT NULL, -- Clé étrangère vers Administrateur
                         FOREIGN KEY (idAdmin) REFERENCES Administrateur(idAdmin)
);

-- Informations générales sur le parking
CREATE TABLE Parking (
                         idParking INTEGER PRIMARY KEY AUTOINCREMENT,
                         nomDuParking VARCHAR(100) NOT NULL,
                         adresseDuParking VARCHAR(255) NOT NULL,
                         nombrePlacesDisponibles INTEGER NOT NULL -- Mise à jour dynamique
);

CREATE TABLE HistoriqueReservation (
                                       idHistorique INTEGER PRIMARY KEY AUTOINCREMENT,   -- Clé primaire unique pour chaque entrée dans l'historique
                                       numeroReservation INTEGER NOT NULL,                  -- Clé étrangère vers la table `RESERVATION`
                                       immatriculation VARCHAR(50) NOT NULL,             -- Numéro d'immatriculation du véhicule
                                       numeroPlace INTEGER NOT NULL,                        -- Clé étrangère vers la table `PLACE`
                                       dateHeureDebut DATETIME NOT NULL,                 -- Date et heure de début de la réservation
                                       dateHeureFin DATETIME NOT NULL,                   -- Date et heure de fin de la réservation
                                       FOREIGN KEY (numeroReservation) REFERENCES RESERVATION(numeroReservation),  -- Référence la table `RESERVATION`
                                       FOREIGN KEY (numeroPlace) REFERENCES PLACE(numero)  -- Référence la table `PLACE`
);
