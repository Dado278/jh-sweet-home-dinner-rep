package it.davide.sweethome.repository.search;

import it.davide.sweethome.domain.Innkeeper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Innkeeper} entity.
 */
public interface InnkeeperSearchRepository extends ElasticsearchRepository<Innkeeper, Long> {
}
