package org.sh.dao;

import org.sh.dto.Organization;

import java.util.List;

public interface OrganizationDao {

    public Organization getOrganization(int id);

    public List<Organization> listOrganizations();

    public boolean editOrganization(Organization organization);

    public boolean deleteOrganization(int organizationId);

    public Organization addOrganization(Organization organization);

    public List<Organization> listOrganizations(int superheroId);

}
