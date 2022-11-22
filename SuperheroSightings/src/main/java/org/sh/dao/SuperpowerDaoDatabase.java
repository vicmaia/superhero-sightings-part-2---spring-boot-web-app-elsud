package org.sh.dao;

import org.sh.dto.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@Profile("prod")
public class SuperpowerDaoDatabase implements SuperpowerDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Superpower getSuperpower(int superpowerId) {
        final String SELECT_SUPERPOWER = "SELECT * FROM superpower WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(SELECT_SUPERPOWER, new SuperpowerMapper(), superpowerId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Superpower> listSuperpowers() {
        final String SELECT_SUPERPOWERS = "SELECT * FROM superpower;";
        return jdbcTemplate.query(SELECT_SUPERPOWERS, new SuperpowerMapper());
    }

    @Override
    public boolean editSuperpower(Superpower superpower) throws NotUniqueException {
        final String UPDATE_SUPERPOWER = "UPDATE superpower SET name = ? WHERE id = ?;";
        try {
            return jdbcTemplate.update(
                    UPDATE_SUPERPOWER, superpower.getName(), superpower.getId()
            ) > 0;
        } catch (DataAccessException ex) {
            throw new NotUniqueException("Superpower name should be unique");
        }
    }

    @Override
    public boolean deleteSuperpower(int superpowerId) throws DeletionException {
        final String DELETE_SUPERPOWER = "DELETE FROM superpower WHERE id = ?;";
        try {
            return jdbcTemplate.update(DELETE_SUPERPOWER, superpowerId) > 0;
        } catch (DataIntegrityViolationException ex) {
            throw new DeletionException("Cannot delete superpower while superheroes with this superpower exist");
        }
    }

    @Override
    public Superpower addSuperpower(Superpower superpower) throws NotUniqueException {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_SUPERPOWER = "INSERT INTO superpower (name) VALUES (?);";
        // check that name is unique
        try {
            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_SUPERPOWER, Statement.RETURN_GENERATED_KEYS
                );
                statement.setString(1, superpower.getName());
                return statement;
            }, keyHolder);
        } catch (DataAccessException ex) {
            throw new NotUniqueException("Superpower name should be unique");
        }
        superpower.setId(keyHolder.getKey().intValue());
        return superpower;
    }

    private final class SuperpowerMapper implements RowMapper<Superpower> {
        @Override
        public Superpower mapRow(ResultSet resultSet, int i) throws SQLException {
            Superpower superpower = new Superpower();
            superpower.setId(resultSet.getInt("id"));
            superpower.setName(resultSet.getString("name"));
            return superpower;
        }
    }
}
