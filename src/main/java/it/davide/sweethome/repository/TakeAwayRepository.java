package it.davide.sweethome.repository;

import it.davide.sweethome.domain.TakeAway;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TakeAway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TakeAwayRepository extends JpaRepository<TakeAway, Long> {
}
