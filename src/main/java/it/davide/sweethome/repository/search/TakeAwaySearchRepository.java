package it.davide.sweethome.repository.search;

import it.davide.sweethome.domain.TakeAway;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link TakeAway} entity.
 */
public interface TakeAwaySearchRepository extends ElasticsearchRepository<TakeAway, Long> {
}
