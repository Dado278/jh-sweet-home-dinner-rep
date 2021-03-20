package it.davide.sweethome.repository.search;

import it.davide.sweethome.domain.InfoInnkeeper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link InfoInnkeeper} entity.
 */
public interface InfoInnkeeperSearchRepository extends ElasticsearchRepository<InfoInnkeeper, Long> {
}
