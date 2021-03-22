package it.davide.sweethome.repository.search;

import it.davide.sweethome.domain.SharedDinner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link SharedDinner} entity.
 */
public interface SharedDinnerSearchRepository extends ElasticsearchRepository<SharedDinner, Long> {
}
