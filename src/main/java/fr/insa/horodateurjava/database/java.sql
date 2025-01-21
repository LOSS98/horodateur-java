create table Administrateur
(
    idAdmin    INTEGER
        primary key autoincrement,
    nom        VARCHAR(100) not null,
    prenom     VARCHAR(100) not null,
    email      VARCHAR(255) not null
        unique,
    motDePasse VARCHAR(255) not null
);

create table Parking
(
    idParking        INTEGER
        primary key autoincrement,
    nomDuParking     VARCHAR(100) not null,
    adresseDuParking VARCHAR(255) not null,
    nombrePlaces     INTEGER      not null
);

create table Place
(
    numero          INTEGER,
    etage           INTEGER     not null,
    type            VARCHAR(50) not null,
    disponibilite   BOOLEAN     not null,
    tarifHoraire    FLOAT       not null,
    puissanceCharge FLOAT,
    enTravaux       BOOLEAN     not null,
    idParking       integer
        constraint Place_Parking
            references Parking,
    primary key (numero, idParking)
);

create table Rapport
(
    idRapport      INTEGER
        primary key autoincrement,
    tauxOccupation FLOAT    not null,
    dateGeneration DATETIME not null,
    emailAdmin     String
        references Administrateur (email),
    totalRecettes  double,
    dateTimeDebut  dateTime,
    dateTimeFin    dateTime
);

create table Reservation
(
    numeroReservation INTEGER
        primary key,
    immatriculation   VARCHAR(50) not null,
    numeroPlace       INTEGER     not null
        references Place (numero),
    dateHeureDebut    DATETIME    not null,
    dateHeureFin      DATETIME    not null,
    idParking         integer     not null
        references Place (idParking),
    prix              double
);

