package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementBanqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementBanque.class);
        PaiementBanque paiementBanque1 = new PaiementBanque();
        paiementBanque1.setId(1L);
        PaiementBanque paiementBanque2 = new PaiementBanque();
        paiementBanque2.setId(paiementBanque1.getId());
        assertThat(paiementBanque1).isEqualTo(paiementBanque2);
        paiementBanque2.setId(2L);
        assertThat(paiementBanque1).isNotEqualTo(paiementBanque2);
        paiementBanque1.setId(null);
        assertThat(paiementBanque1).isNotEqualTo(paiementBanque2);
    }
}
