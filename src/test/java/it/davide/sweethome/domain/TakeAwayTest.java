package it.davide.sweethome.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import it.davide.sweethome.web.rest.TestUtil;

public class TakeAwayTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TakeAway.class);
        TakeAway takeAway1 = new TakeAway();
        takeAway1.setId(1L);
        TakeAway takeAway2 = new TakeAway();
        takeAway2.setId(takeAway1.getId());
        assertThat(takeAway1).isEqualTo(takeAway2);
        takeAway2.setId(2L);
        assertThat(takeAway1).isNotEqualTo(takeAway2);
        takeAway1.setId(null);
        assertThat(takeAway1).isNotEqualTo(takeAway2);
    }
}
