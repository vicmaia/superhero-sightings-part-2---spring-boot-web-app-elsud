package org.sh.dao;

import org.sh.dto.Location;
import org.sh.dto.Sighting;

import org.sh.dto.Superhero;
import org.sh.dto.Superpower;
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
public class SightingDaoImpl implements SightingDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    //review select statements
    @Override
    public Sighting getSighting(int sightingId){
        final String SELECT_SIGHTING = "SELECT * FROM sighting where id = ?;";
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_SIGHTING, new SightingDaoImpl.SightingMapper(), sightingId
            );
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Sighting> listSightings(){
        final String SELECT_SIGHTINGS = "SELECT * FROM sightings;";
        return jdbcTemplate.query(SELECT_SIGHTINGS, new SightingDaoImpl.SightingMapper());
    }

    @Override
    public boolean editSighting(Sighting sighting) throws NotUniqueException {


        final String UPDATE_SIGHTING = "UPDATE sighting SET superheroId = ?, locationId = ?, "
                + "where id =  ?;";
        try {
            return jdbcTemplate.update(
                    UPDATE_SIGHTING, sighting.getLocation(), sighting.getDate(),
                    sighting.getLocation().getId(), sighting.getSuperhero().getId()) > 0;
        } catch (DataAccessException ex) {
            throw new NotUniqueException("Superhero name should be unique");
        }
    }

    @Override
    public boolean deleteSighting(int sightingId){
        final String DELETE_ORGANIZATION_CONN = "DELETE FROM superheroOrganization WHERE superheroId = ?;";
        jdbcTemplate.update(DELETE_ORGANIZATION_CONN, sightingId);
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE superheroId = ?;";
        jdbcTemplate.update(DELETE_SIGHTING, sightingId);
        final String DELETE_HERO = "DELETE FROM superhero WHERE id = ?;";
        return jdbcTemplate.update(DELETE_HERO, sightingId) > 0;
    }

    @Override
    public Sighting addSighting(Sighting sighting) throws NotUniqueException {
        GeneratedKeyHolder keyHolder = new  GeneratedKeyHolder();
        final String INSERT_SIGHTING = "INSERT INTO sighting "
                + "(locationId, superheroId, date) VALUES(?, ?, ?);";
        try {
            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_SIGHTING, Statement.RETURN_GENERATED_KEYS
                );
                statement.setInt(1, sighting.getLocation().getId());
                statement.setInt(2, sighting.getSuperhero().getId());
                statement.setDate(3, (Date) sighting.getDate());
                return statement;
            }, keyHolder);
        } catch (DataAccessException ex) {
            throw new NotUniqueException("Sighting name should be unique");
        }
        sighting.setId((Date)keyHolder.getKey().intValue());
        return sighting;
    }

    @Override
    public List<Sighting> listSightings(LocalDateTime date){
        return jdbcTemplate.query("select * from sightings",
                new SightingMapper());
    }

    @Override
    public List<Sighting> listLastSightings(){
        //implement
    }

    private final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet resultSet, int i) throws SQLException {
            Sighting sighting = new Sighting();
            Location location = new Location();
            Superhero superhero = new Superhero();

            location.setId(resultSet.getInt("location.id"));
            superhero.setId(resultSet.getInt("superhero.id"));
            sighting.setDate(resultSet.getDate("sighting.date"));
            /*
            location.setName(resultSet.getString("location.name"));
            location.setDescription(resultSet.getString("location.description"));
            location.setAddress(resultSet.getString("location.address"));
            location.setLatitude(resultSet.getString("location.latitude"));
            location.setLongitude(resultSet.getString("location.longitude"));
             */

            sighting.setLocation(location);
            sighting.setSuperhero(superhero);
            return sighting;
        }
    }

    /*
    private final class SightingShortMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet resultSet, int i) throws SQLException {
            Sighting sighting = new Sighting();
            superhero.setId(resultSet.getInt("id"));
            superhero.setName(resultSet.getString("name"));
            superhero.setDescription(resultSet.getString("description"));
            return sighting;
        }
    }

     */

}
