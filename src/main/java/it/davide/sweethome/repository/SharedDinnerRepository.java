package it.davide.sweethome.repository;

import it.davide.sweethome.domain.SharedDinner;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SharedDinner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SharedDinnerRepository extends JpaRepository<SharedDinner, Long> {
}
