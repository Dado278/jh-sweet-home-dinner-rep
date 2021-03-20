package it.davide.sweethome.repository;

import it.davide.sweethome.domain.InfoInnkeeper;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InfoInnkeeper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfoInnkeeperRepository extends JpaRepository<InfoInnkeeper, Long> {
}
