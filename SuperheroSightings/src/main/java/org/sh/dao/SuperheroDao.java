package org.sh.dao;

import org.sh.dto.Superhero;

import java.util.List;

public interface SuperheroDao {

    public Superhero getSuperhero(int id);

    public List<Superhero> listSuperhero();

    public boolean editSuperhero(Superhero superhero) throws NotUniqueException;

    public boolean deleteSuperhero(int superheroId);

    public Superhero addSuperhero(Superhero superhero) throws NotUniqueException;

    public List<Superhero> listSuperheroForLocation(int locationId);

    public List<Superhero> listSuperheroForOrganization(int organizationId);
}
