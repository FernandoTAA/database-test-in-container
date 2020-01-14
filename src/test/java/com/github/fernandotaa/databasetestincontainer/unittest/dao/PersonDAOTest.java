package com.github.fernandotaa.databasetestincontainer.unittest.dao;

import com.github.fernandotaa.databasetestincontainer.dao.PersonDAO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PersonDAOTest {

    @Autowired
    private PersonDAO personDAO;

    @Test
    public void findAll() {
        var list = personDAO.findAll();
        Assertions.assertThat(list).hasSize(28);
    }
}
