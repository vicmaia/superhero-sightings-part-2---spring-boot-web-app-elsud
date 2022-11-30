package org.sh.dao;

import org.sh.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("prod")
public class OrganizationDaoImpl implements OrganizationDao {
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Organization getOrganization(int id) {
        final String SELECT_ORGANIZATION = "SELECT * FROM organization where id = ?;";
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_ORGANIZATION, new OrganizationDaoImpl.OrganizationMapper(), id
            );
        } catch (DataAccessException ex) {
            return null;
        }
    }


    @Override
    public List<Organization> listOrganizations() {
        final String SELECT_ORGANIZATIONS = "SELECT * FROM organization;";
        return jdbcTemplate.query(SELECT_ORGANIZATIONS, new OrganizationDaoImpl.OrganizationMapper());
    }


    @Override
    public boolean editOrganization(Organization organization) throws NotUniqueException {
        final String UPDATE_ORGANIZATION = "UPDATE organization SET name = ?, description = ?, "
                + "address = ? WHERE id = ?;";
        try {
            return jdbcTemplate.update(
                    UPDATE_ORGANIZATION, organization.getName(), organization.getDescription(),
                    organization.getAddress(), organization.getId()) > 0;
        } catch (DataAccessException ex) {
            throw new NotUniqueException("Organization name should be unique");
        }
    }


    //review
    @Override
    public boolean deleteOrganization(int organizationId) {
        final String DELETE_ORGANIZATION_CONN = "DELETE FROM superheroOrganization WHERE superheroId = ?;";
        jdbcTemplate.update(DELETE_ORGANIZATION_CONN, organizationId);
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE superheroId = ?;";
        jdbcTemplate.update(DELETE_SIGHTING, organizationId);
        final String DELETE_HERO = "DELETE FROM superhero WHERE id = ?;";
        return jdbcTemplate.update(DELETE_HERO, organizationId) > 0;
    }


    @Override
    public Organization addOrganization(Organization organization) throws NotUniqueException {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_ORGANIZATION = "INSERT INTO organization "
                + "(id, name, description, address) VALUES(?, ?, ?, ?);";
        try {
            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_ORGANIZATION, Statement.RETURN_GENERATED_KEYS
                );
                statement.setInt(1, organization.getId());
                statement.setString(2, organization.getName());
                statement.setString(3, organization.getDescription());
                statement.setString(4, organization.getAddress());
                return statement;
            }, keyHolder);
        } catch (DataAccessException ex) {
            throw new NotUniqueException("Sighting name should be unique");
        }
        organization.setId(keyHolder.getKey().intValue());
        return organization;
    }


    @Override
    public List<Organization> listOrganizations(int superheroId) {
        return jdbcTemplate.query("select * from organization",
                new OrganizationDaoImpl.OrganizationMapper());
    }


    private final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet resultSet, int i) throws SQLException {
            Organization organization = new Organization();

            organization.setId(resultSet.getInt("organization.id"));
            organization.setName(resultSet.getString("superhero.name"));
            organization.setDescription(resultSet.getString("sighting.description"));
            organization.setAddress(resultSet.getString("sighting.address"));


            return organization;
        }
    }
}

