package it.davide.sweethome.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link InnkeeperSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class InnkeeperSearchRepositoryMockConfiguration {

    @MockBean
    private InnkeeperSearchRepository mockInnkeeperSearchRepository;

}
