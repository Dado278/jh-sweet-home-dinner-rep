package it.davide.sweethome.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TakeAwaySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TakeAwaySearchRepositoryMockConfiguration {

    @MockBean
    private TakeAwaySearchRepository mockTakeAwaySearchRepository;

}
