package it.davide.sweethome.repository.search;

import it.davide.sweethome.domain.People;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link People} entity.
 */
public interface PeopleSearchRepository extends ElasticsearchRepository<People, Long> {
}
