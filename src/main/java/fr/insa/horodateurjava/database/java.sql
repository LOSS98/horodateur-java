CREATE TABLE Place (
                       numero INTEGER NOT NULL ,
                       etage INTEGER NOT NULL,
                       idParking INTEGER NOT NULL,
                       type VARCHAR(50) NOT NULL,
                       disponibilite BOOLEAN NOT NULL,
                       tarifHoraire FLOAT NOT NULL,
                       puissanceCharge FLOAT,
                       enTravaux BOOLEAN NOT NULL
                       PRIMARY KEY (idParking, numero)
                       FOREIGN KEY (idParking) REFERENCES Parking(idParking)
);

CREATE TABLE Reservation (
                             numeroReservation INTEGER PRIMARY KEY,
                             immatriculation VARCHAR(50) NOT NULL,
                             numeroPlace INTEGER NOT NULL,
                             idParking INTEGER NOT NULL
                             dateHeureDebut DATETIME NOT NULL,
                             dateHeureFin DATETIME NOT NULL,
                             FOREIGN KEY (numeroPlace) REFERENCES Place(numero),
                             FOREIGN KEY (idParking) REFERENCES Place(idParking)
);

CREATE TABLE Administrateur (
                                idAdmin INTEGER PRIMARY KEY AUTOINCREMENT,
                                nom VARCHAR(100) NOT NULL,
                                prenom VARCHAR(100) NOT NULL,
                                email VARCHAR(255) UNIQUE NOT NULL,
                                motDePasse VARCHAR(255) NOT NULL
);

CREATE TABLE Rapport (
                         idRapport INTEGER PRIMARY KEY AUTOINCREMENT,
                         tauxOccupation FLOAT NOT NULL,
                         heuresDePointe JSON,
                         dateGeneration DATETIME NOT NULL,
                         idAdmin INTEGER NOT NULL,
                         FOREIGN KEY (idAdmin) REFERENCES Administrateur(idAdmin)
);

CREATE TABLE Parking (
                         idParking INTEGER PRIMARY KEY AUTOINCREMENT,
                         nomDuParking VARCHAR(100) NOT NULL,
                         adresseDuParking VARCHAR(255) NOT NULL,
                         nombrePlaces INTEGER NOT NULL
);

CREATE TABLE HistoriqueReservation (
                                       idHistorique INTEGER PRIMARY KEY AUTOINCREMENT,
                                       numeroReservation INTEGER NOT NULL,
                                       immatriculation VARCHAR(50) NOT NULL,
                                       numeroPlace INTEGER NOT NULL,
                                       dateHeureDebut DATETIME NOT NULL,
                                       dateHeureFin DATETIME NOT NULL,
                                       FOREIGN KEY (numeroReservation) REFERENCES RESERVATION(numeroReservation),
                                       FOREIGN KEY (numeroPlace) REFERENCES PLACE(numero)
);
