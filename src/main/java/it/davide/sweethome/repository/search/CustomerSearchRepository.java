package it.davide.sweethome.repository.search;

import it.davide.sweethome.domain.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Customer} entity.
 */
public interface CustomerSearchRepository extends ElasticsearchRepository<Customer, Long> {
}
