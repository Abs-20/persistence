package com.example.persistence;

import com.example.persistence.dao.JpaOfficerDAO;
import com.example.persistence.entities.Officer;
import com.example.persistence.entities.Rank;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//hibernate by default generate the schema from domain model, so it overwrote the whole schema
// to avoid that we need to add spring.jpa.hibernate.ddl-auto=none at property file
@SpringBootTest
@Transactional
public class JpaOfficerDAOTest {
    @Autowired
    private JpaOfficerDAO dao;
    @Autowired
    private JdbcTemplate template;

    //methods to retrieve all the ids from the database
    private List<Integer> getIds(){
        return template.query("select id from officers",(rs,num) -> rs.getInt("id"));
    }

    @Test
    public void testSave() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = dao.save(officer);
        assertNotNull(officer.getId());
    }

    @Test
    public void findOneThatExists() {
        getIds().forEach(id -> {
            Optional<Officer> officer = dao.findById(id);
            assertTrue(officer.isPresent());
            assertEquals(id, officer.get().getId());
        });
    }

    @Test
    public void findOneThatDoesNotExist() {
        Optional<Officer> officer = dao.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    public void findAll() {
        List<String> dbNames = dao.findAll().stream()
                .map(Officer::getLastName)
                .collect(Collectors.toList());
        assertThat(dbNames).contains("Kirk", "Picard", "Sisko", "Janeway", "Archer");
    }

    @Test
    public void count() {
        assertEquals(5, dao.count());
    }

    @Test
    public void delete() {
        getIds().forEach(id -> {
            Optional<Officer> officer = dao.findById(id);
            assertTrue(officer.isPresent());
            dao.delete(officer.get());
        });
        assertEquals(0, dao.count());
    }

    @Test
    public void existsById() {
        getIds().forEach(id -> assertTrue(dao.existsById(id)));
    }

    @Test
    public void doesNotExist() {
        List<Integer> ids = getIds();
        assertThat(ids).doesNotContain(999);
        assertFalse(dao.existsById(999));
    }

}


