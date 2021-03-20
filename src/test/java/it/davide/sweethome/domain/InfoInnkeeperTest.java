package it.davide.sweethome.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import it.davide.sweethome.web.rest.TestUtil;

public class InfoInnkeeperTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoInnkeeper.class);
        InfoInnkeeper infoInnkeeper1 = new InfoInnkeeper();
        infoInnkeeper1.setId(1L);
        InfoInnkeeper infoInnkeeper2 = new InfoInnkeeper();
        infoInnkeeper2.setId(infoInnkeeper1.getId());
        assertThat(infoInnkeeper1).isEqualTo(infoInnkeeper2);
        infoInnkeeper2.setId(2L);
        assertThat(infoInnkeeper1).isNotEqualTo(infoInnkeeper2);
        infoInnkeeper1.setId(null);
        assertThat(infoInnkeeper1).isNotEqualTo(infoInnkeeper2);
    }
}
