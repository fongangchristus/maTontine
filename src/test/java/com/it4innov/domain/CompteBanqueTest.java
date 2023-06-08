package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteBanqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteBanque.class);
        CompteBanque compteBanque1 = new CompteBanque();
        compteBanque1.setId(1L);
        CompteBanque compteBanque2 = new CompteBanque();
        compteBanque2.setId(compteBanque1.getId());
        assertThat(compteBanque1).isEqualTo(compteBanque2);
        compteBanque2.setId(2L);
        assertThat(compteBanque1).isNotEqualTo(compteBanque2);
        compteBanque1.setId(null);
        assertThat(compteBanque1).isNotEqualTo(compteBanque2);
    }
}
