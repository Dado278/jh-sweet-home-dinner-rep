package it.davide.sweethome.repository;

import it.davide.sweethome.domain.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "select distinct customer from Customer customer left join fetch customer.sharedDinners",
        countQuery = "select count(distinct customer) from Customer customer")
    Page<Customer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct customer from Customer customer left join fetch customer.sharedDinners")
    List<Customer> findAllWithEagerRelationships();

    @Query("select customer from Customer customer left join fetch customer.sharedDinners where customer.id =:id")
    Optional<Customer> findOneWithEagerRelationships(@Param("id") Long id);
}
