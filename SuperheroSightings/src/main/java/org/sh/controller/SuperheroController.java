package org.sh.controller;

import org.sh.dao.NotUniqueException;
import org.sh.dao.SuperheroDao;
import org.sh.dao.SuperpowerDao;
import org.sh.dto.Superhero;
import org.sh.dto.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/superhero")
public class SuperheroController {

    private final SuperheroDao shDao;
    private final SuperpowerDao spDao;

    @Autowired
    public SuperheroController(SuperheroDao shDao, SuperpowerDao spDao) {
        this.shDao = shDao;
        this.spDao = spDao;
    }

    @GetMapping("")
    public List<Superhero> listSuperhero() {
        return shDao.listSuperhero();
    }

    @GetMapping("/forLocation/{locationId}")
    public List<Superhero> listSuperheroForLocation(@PathVariable int locationId) {
        return shDao.listSuperheroForLocation(locationId);
    }

    @GetMapping("/forOrganization/{organizationId}")
    public List<Superhero> listSuperheroForOrganization(@PathVariable int organizationId) {
        return shDao.listSuperheroForOrganization(organizationId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Superhero> getSuperhero(@PathVariable int id) {
        Superhero superhero = shDao.getSuperhero(id);
        if (superhero == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(superhero);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSuperhero(@PathVariable int id) {
        if (shDao.deleteSuperhero(id)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateSuperhero(
            @PathVariable int id, @RequestParam String name, @RequestParam String description,
            @RequestParam int superpowerId) throws NotUniqueException {
        Superpower superpower = spDao.getSuperpower(superpowerId);
        if (superpower == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Superhero superhero = new Superhero(
                id, name, description, superpower.getId(), superpower.getName());
        if (shDao.editSuperhero(superhero)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<Superhero> addSuperhero(
            @RequestParam String name, @RequestParam String description,
            @RequestParam int superpowerId) throws NotUniqueException {
        Superpower superpower = spDao.getSuperpower(superpowerId);
        if (superpower == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Superhero superhero = new Superhero(
                name, description, superpower.getId(), superpower.getName());
        return new ResponseEntity<>(shDao.addSuperhero(superhero), HttpStatus.CREATED);
    }

}
