package com.example.persistence.dao;

import com.example.persistence.entities.Officer;
import com.example.persistence.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer,Integer> {

    // in this repository from spring we can use all the crud methods (like what we did in OfficerDAO
// we can create our own methods here and use it

    List<Officer> findAllByRankAndLastNameContaining(Rank rank, String string);
}
