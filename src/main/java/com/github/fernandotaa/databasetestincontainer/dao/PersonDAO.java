package com.github.fernandotaa.databasetestincontainer.dao;

import com.github.fernandotaa.databasetestincontainer.dto.PersonDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDAO  extends CrudRepository<PersonDTO, Integer> {
    @Query("SELECT * FROM PERSON")
    List<PersonDTO> findAll();
}
