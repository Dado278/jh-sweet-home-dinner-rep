package it.davide.sweethome.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.davide.sweethome.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SharedDinnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SharedDinner.class);
        SharedDinner sharedDinner1 = new SharedDinner();
        sharedDinner1.setId(1L);
        SharedDinner sharedDinner2 = new SharedDinner();
        sharedDinner2.setId(sharedDinner1.getId());
        assertThat(sharedDinner1).isEqualTo(sharedDinner2);
        sharedDinner2.setId(2L);
        assertThat(sharedDinner1).isNotEqualTo(sharedDinner2);
        sharedDinner1.setId(null);
        assertThat(sharedDinner1).isNotEqualTo(sharedDinner2);
    }
}
