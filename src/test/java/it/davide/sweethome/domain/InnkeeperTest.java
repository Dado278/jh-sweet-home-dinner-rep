package it.davide.sweethome.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.davide.sweethome.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InnkeeperTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Innkeeper.class);
        Innkeeper innkeeper1 = new Innkeeper();
        innkeeper1.setId(1L);
        Innkeeper innkeeper2 = new Innkeeper();
        innkeeper2.setId(innkeeper1.getId());
        assertThat(innkeeper1).isEqualTo(innkeeper2);
        innkeeper2.setId(2L);
        assertThat(innkeeper1).isNotEqualTo(innkeeper2);
        innkeeper1.setId(null);
        assertThat(innkeeper1).isNotEqualTo(innkeeper2);
    }
}
