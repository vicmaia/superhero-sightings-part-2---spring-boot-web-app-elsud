package org.sh.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sh.dto.Superhero;
import org.sh.dto.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SuperheroDaoDatabaseImplTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SuperpowerDao superpowerDao;

    @Autowired
    SuperheroDao superheroDao;

    @Before
    public void setUp() {
        final String DELETE_SIGHTINGS = "DELETE FROM sighting;";
        final String DELETE_SUPERHERO_ORGANIZATION = "DELETE FROM superheroOrganization;";
        final String DELETE_SUPERHERO = "DELETE FROM superhero;";
        final String DELETE_SUPERPOWER = "DELETE FROM superpower;";
        jdbcTemplate.update(DELETE_SIGHTINGS);
        jdbcTemplate.update(DELETE_SUPERHERO_ORGANIZATION);
        jdbcTemplate.update(DELETE_SUPERHERO);
        jdbcTemplate.update(DELETE_SUPERPOWER);
    }

    @Test
    public void getNotExistingSuperhero() {
        assertNull(superheroDao.getSuperhero(12));
    }

    @Test
    public void getAndAddSuperhero() {
        Superpower superpower = createSuperpower();
        Superhero superhero = new Superhero(
                "name", "description", superpower.getId(), superpower.getName());
        Superhero addedSuperhero = null;
        try {
            addedSuperhero = superheroDao.addSuperhero(superhero);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        assertEquals(superhero, addedSuperhero);
        Superhero receivedSuperhero = superheroDao.getSuperhero(superhero.getId());
        assertEquals(addedSuperhero, receivedSuperhero);
    }

    @Test
    public void addSuperheroWithNotUniqueName() {
        Superpower superpower = createSuperpower();
        Superhero superhero = new Superhero(
                "name", "description", superpower.getId(), superpower.getName()
        );
        try {
            superheroDao.addSuperhero(superhero);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        assertThrows(NotUniqueException.class, ()-> superheroDao.addSuperhero(superhero));
    }

    @Test
    public void listSuperhero() {
        Superpower superpower = createSuperpower();
        Superhero superhero1 = new Superhero(
                "name1", "description1", superpower.getId(), superpower.getName()
        );
        Superhero superhero2 = new Superhero(
                "name2", "description2", superpower.getId(), superpower.getName()
        );
        try {
            superheroDao.addSuperhero(superhero1);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        List<Superhero> superheroList = superheroDao.listSuperhero();
        assertEquals(1, superheroList.size());
        assertTrue(superheroList.contains(superhero1));
        assertFalse(superheroList.contains(superhero2));
        try {
            superheroDao.addSuperhero(superhero2);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        superheroList = superheroDao.listSuperhero();
        assertEquals(2, superheroList.size());
        assertTrue(superheroList.contains(superhero2));
    }

    @Test
    public void editSuperhero() {
        Superpower superpower = createSuperpower();
        Superhero superhero = new Superhero(
                "name", "description1", superpower.getId(), superpower.getName()
        );
        try {
            superhero = superheroDao.addSuperhero(superhero);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        Superhero receivedSuperhero = superheroDao.getSuperhero(superhero.getId());
        assertEquals(superhero, receivedSuperhero);
        superhero.setName("newName");
        superhero.setDescription("newDescription");
        assertNotEquals(superhero, receivedSuperhero);
        try {
            boolean isEdited = superheroDao.editSuperhero(superhero);
            assertTrue(isEdited);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        assertEquals(superhero, superheroDao.getSuperhero(superhero.getId()));
    }

    @Test
    public void editSuperheroWithNotUniqueName() {
        Superpower superpower = createSuperpower();
        Superhero superhero = new Superhero(
                "name1", "description1", superpower.getId(), superpower.getName()
        );
        Superhero superhero2 = new Superhero(
                "name2", "description2", superpower.getId(), superpower.getName()
        );
        try {
            superhero = superheroDao.addSuperhero(superhero);
            superheroDao.addSuperhero(superhero2);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
        Superhero receivedSuperhero = superheroDao.getSuperhero(superhero.getId());
        assertEquals(superhero, receivedSuperhero);
        superhero.setName(superhero2.getName());
        try {
            superheroDao.editSuperhero(superhero);
            fail("Should fail for not unique name");
        } catch (NotUniqueException ex) {
        }
    }

    @Test
    public void editNotExistingSuperhero() {
        Superpower superpower = createSuperpower();
        Superhero superhero = new Superhero(
                12, "name", "description", superpower
        );
        try {
            boolean isEdited = superheroDao.editSuperhero(superhero);
            assertFalse(isEdited);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique name");
        }
    }

    @Test
    public void deleteNotExistingSuperhero() {
        assertFalse(superheroDao.deleteSuperhero(12));
    }

    @Test
    public void deleteSuperhero() {
    }

    @Test
    public void listSuperheroForLocation() {
    }

    @Test
    public void listSuperheroForOrganization() {
    }

    private Superpower createSuperpower() {
        Superpower superpower = new Superpower("power");
        try {
            superpower = superpowerDao.addSuperpower(superpower);
        } catch (NotUniqueException ex) {
            fail("Should not fail for unique superpower");
        }
        return superpower;
    }
}