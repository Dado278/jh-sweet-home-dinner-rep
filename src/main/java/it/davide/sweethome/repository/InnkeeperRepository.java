package it.davide.sweethome.repository;

import it.davide.sweethome.domain.Innkeeper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Innkeeper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InnkeeperRepository extends JpaRepository<Innkeeper, Long> {}
