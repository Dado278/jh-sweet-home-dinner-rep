package it.davide.sweethome.config;

import java.io.File;
import org.assertj.core.util.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ElasticsearchTestConfiguration {

    @Autowired
    public void elasticsearchProperties(ElasticsearchProperties elasticsearchProperties) {
        File tempdir = Files.newTemporaryFolder();
        elasticsearchProperties.getProperties().put("path.home", tempdir.getAbsolutePath());
    }
}
