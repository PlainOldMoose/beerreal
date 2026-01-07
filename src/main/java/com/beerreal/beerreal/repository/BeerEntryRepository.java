package com.beerreal.beerreal.repository;

import com.beerreal.beerreal.model.BeerEntry;
import com.beerreal.beerreal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerEntryRepository extends JpaRepository<BeerEntry, Integer> {
    List<BeerEntry> findByUserOrderByCreatedAtDesc(User user);
    List<BeerEntry> findByYearOrderByCreatedAtDesc(Integer year);

    @Query("SELECT e.user, COUNT(e) as entryCount FROM BeerEntry e WHERE e.year = :year GROUP BY e.user ORDER BY entryCount DESC")
    List<Object[]> findLeaderboardByYear(Integer year);

    long countByUserAndYear(User user, Integer year);
}
