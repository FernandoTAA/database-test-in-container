package com.github.fernandotaa.databasetestincontainer.controller;

import com.github.fernandotaa.databasetestincontainer.dto.PersonDTO;
import com.github.fernandotaa.databasetestincontainer.dao.PersonDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonDAO personDAO;

    public PersonController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping("/")
    public ResponseEntity<List<PersonDTO>> getPeople() {
        return ResponseEntity.ok(personDAO.findAll());
    }
}
