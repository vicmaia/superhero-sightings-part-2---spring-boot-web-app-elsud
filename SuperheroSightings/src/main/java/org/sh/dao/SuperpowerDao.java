package org.sh.dao;

import org.sh.dto.Superpower;

import java.util.List;

public interface SuperpowerDao {

    public Superpower getSuperpower(int superpowerId);

    public List<Superpower> listSuperpowers();

    public boolean editSuperpower(Superpower superpower) throws NotUniqueException;

    public boolean deleteSuperpower(int superpowerId) throws DeletionException;

    public Superpower addSuperpower(Superpower superpower) throws NotUniqueException;
}
