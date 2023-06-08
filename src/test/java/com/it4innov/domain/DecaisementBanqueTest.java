package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DecaisementBanqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecaisementBanque.class);
        DecaisementBanque decaisementBanque1 = new DecaisementBanque();
        decaisementBanque1.setId(1L);
        DecaisementBanque decaisementBanque2 = new DecaisementBanque();
        decaisementBanque2.setId(decaisementBanque1.getId());
        assertThat(decaisementBanque1).isEqualTo(decaisementBanque2);
        decaisementBanque2.setId(2L);
        assertThat(decaisementBanque1).isNotEqualTo(decaisementBanque2);
        decaisementBanque1.setId(null);
        assertThat(decaisementBanque1).isNotEqualTo(decaisementBanque2);
    }
}
