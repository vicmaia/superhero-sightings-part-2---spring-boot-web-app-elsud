DROP DATABASE IF EXISTS SuperheroSighting;
CREATE DATABASE SuperheroSighting;
USE SuperheroSighting;

CREATE TABLE superpower (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) UNIQUE NOT NULL
);
CREATE TABLE superhero (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(40) UNIQUE NOT NULL,
    description VARCHAR(160),
    superpower INT NOT NULL,
    CONSTRAINT fk_superhero_superpower FOREIGN KEY (superpower) REFERENCES superpower(id)
);
CREATE TABLE organization (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(160),
    address VARCHAR(80)
);
CREATE TABLE superheroOrganization (
	superheroId INT,
    organizationId INT,
    CONSTRAINT pk_superheroOrganization PRIMARY KEY (superheroId, organizationId),
    CONSTRAINT fk_superheroOrganization_superhero FOREIGN KEY (superheroId) REFERENCES superhero(id),
    CONSTRAINT fk_superheroOrganization_organization FOREIGN KEY (organizationId) REFERENCES organization(id)
);
CREATE TABLE location (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(40) NOT NULL,
    description VARCHAR(160),
    address VARCHAR(80) NOT NULL,
    longitude VARCHAR(10) NOT NULL,
    latitude VARCHAR(10) NOT NULL,
    CONSTRAINT UNIQUE (longitude, latitude)
);
CREATE TABLE sighting (
	id INT AUTO_INCREMENT PRIMARY KEY,
	superheroId INT NOT NULL,
    locationId INT NOT NULL,
    date TIMESTAMP NOT NULL,
    CONSTRAINT UNIQUE (date, superheroId, locationId),
    CONSTRAINT fk_sighting_superhero FOREIGN KEY (superheroId) REFERENCES superhero(Id),
    CONSTRAINT fk_sighting_location FOREIGN KEY (locationId) REFERENCES location(id)
);