package org.sh.controller;

import org.sh.dao.DeletionException;
import org.sh.dao.NotUniqueException;
import org.sh.dao.SuperpowerDao;
import org.sh.dto.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/superpower")
public class SuperpowerController {

    private final SuperpowerDao spDao;

    @Autowired
    public SuperpowerController(SuperpowerDao superpowerDao) {
        this.spDao = superpowerDao;
    }

    @GetMapping("")
    public List<Superpower> listSuperpowers() {
        return spDao.listSuperpowers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Superpower> getSuperpower(@PathVariable int id) {
        Superpower superpower = spDao.getSuperpower(id);
        if (superpower == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(superpower);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSuperpower(@PathVariable int id) throws DeletionException {
        if (spDao.deleteSuperpower(id)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateSuperpower(@PathVariable int id, @RequestBody Superpower superpower) throws NotUniqueException {
        superpower.setId(id);
        if (spDao.editSuperpower(superpower)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Superpower addSuperpower(@RequestBody Superpower superpower) throws NotUniqueException {
        return spDao.addSuperpower(superpower);
    }

}
