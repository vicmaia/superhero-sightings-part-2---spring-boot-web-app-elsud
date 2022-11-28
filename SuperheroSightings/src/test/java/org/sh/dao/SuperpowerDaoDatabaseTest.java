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
public class SuperpowerDaoDatabaseTest {

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
    public void addAndGetSuperpower() {
        // test adding unique superpower
        Superpower superpower = new Superpower("testName");
        Superpower addedSuperpower = null;
        try {
            addedSuperpower = superpowerDao.addSuperpower(superpower);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        Superpower receivedSuperpower = superpowerDao.getSuperpower(addedSuperpower.getId());
        assertEquals(addedSuperpower, receivedSuperpower);
        assertEquals(superpower.getName(), receivedSuperpower.getName());
        // test adding not unique superpower
        assertThrows(NotUniqueException.class, () -> superpowerDao.addSuperpower(superpower));
    }

    @Test
    public void getNotExistingSuperpower() {
        assertNull(superpowerDao.getSuperpower(14));
    }

    @Test
    public void listSuperpowers() {
        Superpower superpower1 = new Superpower("testName1");
        Superpower superpower2 = new Superpower("testName2");
        // test after adding 1
        try {
            superpowerDao.addSuperpower(superpower1);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        List<Superpower> superpowerList = superpowerDao.listSuperpowers();
        assertEquals(1, superpowerList.size());
        assertTrue(superpowerList.contains(superpower1));
        assertFalse(superpowerList.contains(superpower2));
        // test after adding both
        try {
            superpowerDao.addSuperpower(superpower2);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        superpowerList = superpowerDao.listSuperpowers();
        assertEquals(2, superpowerList.size());
        assertTrue(superpowerList.contains(superpower2));
    }

    @Test
    public void editSuperpower() {
        // add superpower
        Superpower superpower = new Superpower("testName");
        Superpower addedSuperpower = null;
        try {
            addedSuperpower = superpowerDao.addSuperpower(superpower);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        // edit superpower
        addedSuperpower.setName("newName");
        // check that superpower in db has previous name
        Superpower receivedSuperpower = superpowerDao.getSuperpower(addedSuperpower.getId());
        assertNotEquals(addedSuperpower.getName(), receivedSuperpower.getName());
        // edit superpower in db and test that names are similar
        try {
            superpowerDao.editSuperpower(addedSuperpower);
            receivedSuperpower = superpowerDao.getSuperpower(addedSuperpower.getId());
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        assertEquals(addedSuperpower.getName(), receivedSuperpower.getName());
    }

    @Test
    public void editSuperpowerWithNotUniqueName() {
        Superpower superpower = null;
        try {
            // add superpower with "testName"
            superpower = superpowerDao.addSuperpower(new Superpower("testName"));
            // add superpower with "newName"
            superpowerDao.addSuperpower(new Superpower("newName"));
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        // set "testName" to "newName" and test that it cannot be added to db
        superpower.setName("newName");
        try {
            superpowerDao.editSuperpower(superpower);
            fail("Should fail for not unique name");
        } catch (NotUniqueException ex) {
        }
    }

    @Test
    public void editNotExistingSuperpower() {
        // test that no line was modified
        Superpower superpower = new Superpower("testName");
        superpower.setId(15);
        try {
            boolean isModified = superpowerDao.editSuperpower(superpower);
            assertFalse(isModified);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
    }

    @Test
    public void deleteNotExistingSuperpower() {
        // test that no line was deleted
        try {
            boolean isDeleted = superpowerDao.deleteSuperpower(13);
        } catch (DeletionException ex) {
            fail("Not existing superpower cannot have dependent tables");
        }
    }

    @Test
    public void deleteSuperpower() {
        // add superpower
        Superpower superpower = new Superpower("testName");
        try {
            superpower = superpowerDao.addSuperpower(superpower);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        // test that superpower exists
        Superpower receivedSuperpower = superpowerDao.getSuperpower(superpower.getId());
        assertNotNull(receivedSuperpower);
        assertEquals(superpower, receivedSuperpower);
        // delete and test that line was deleted
        try {
            boolean isDeleted = superpowerDao.deleteSuperpower(superpower.getId());
            assertTrue(isDeleted);
        } catch (DeletionException ex) {
            fail("Not existing superpower cannot have dependent tables");
        }
        // test that superpower doesn't exist
        receivedSuperpower = superpowerDao.getSuperpower(superpower.getId());
        assertNull(receivedSuperpower);
    }

    @Test
    public void deleteSuperpowerWithDependentSuperhero() {
        // add superpower
        Superpower superpower = new Superpower("testName");
        try {
            Superpower addedSuperpower = superpowerDao.addSuperpower(superpower);
            superpower.setId(addedSuperpower.getId());
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique name");
        }
        // add superhero
        Superhero superhero = new Superhero(
                "name", "description", superpower.getId(), superpower.getName());
        try {
            superhero = superheroDao.addSuperhero(superhero);
        } catch (NotUniqueException ex) {
            fail("Shouldn't fail for unique superhero name");
        }
        // test that cannot delete
        assertThrows(DeletionException.class, () -> superpowerDao.deleteSuperpower(superpower.getId()));
        // test that superpower exists
        Superpower receivedSuperpower = superpowerDao.getSuperpower(superpower.getId());
        assertNotNull(receivedSuperpower);
        assertEquals(superpower, receivedSuperpower);
    }

}