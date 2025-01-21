package com.mcommandes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mcommandes.model.Commande;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

	@Query("SELECT c FROM Commande c WHERE c.date >= :startDate")
    List<Commande> findRecentCommandes(@Param("startDate") LocalDate startDate);
}
