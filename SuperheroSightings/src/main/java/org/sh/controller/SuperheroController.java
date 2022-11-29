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
            @PathVariable int id, @RequestBody SuperheroFromRequestBody sh) throws NotUniqueException {
        Superpower superpower = spDao.getSuperpower(sh.getSuperpowerId());
        if (superpower == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Superhero superhero = new Superhero(
                id, sh.getName(), sh.getDescription(), superpower);
        if (shDao.editSuperhero(superhero)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<Superhero> addSuperhero(@RequestBody SuperheroFromRequestBody sh) throws NotUniqueException {
        Superpower superpower = spDao.getSuperpower(sh.getSuperpowerId());
        if (superpower == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Superhero superhero = new Superhero(sh.getName(), sh.getDescription(), superpower);
        return new ResponseEntity<>(shDao.addSuperhero(superhero), HttpStatus.CREATED);
    }

    private static class SuperheroFromRequestBody {
        private int id;
        private String name;
        private String description;
        private int superpowerId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSuperpowerId() {
            return superpowerId;
        }

        public void setSuperpowerId(int superpowerId) {
            this.superpowerId = superpowerId;
        }
    }

}
