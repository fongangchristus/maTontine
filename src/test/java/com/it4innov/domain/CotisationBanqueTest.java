package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CotisationBanqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CotisationBanque.class);
        CotisationBanque cotisationBanque1 = new CotisationBanque();
        cotisationBanque1.setId(1L);
        CotisationBanque cotisationBanque2 = new CotisationBanque();
        cotisationBanque2.setId(cotisationBanque1.getId());
        assertThat(cotisationBanque1).isEqualTo(cotisationBanque2);
        cotisationBanque2.setId(2L);
        assertThat(cotisationBanque1).isNotEqualTo(cotisationBanque2);
        cotisationBanque1.setId(null);
        assertThat(cotisationBanque1).isNotEqualTo(cotisationBanque2);
    }
}
