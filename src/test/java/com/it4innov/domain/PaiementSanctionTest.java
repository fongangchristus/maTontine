package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementSanctionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementSanction.class);
        PaiementSanction paiementSanction1 = new PaiementSanction();
        paiementSanction1.setId(1L);
        PaiementSanction paiementSanction2 = new PaiementSanction();
        paiementSanction2.setId(paiementSanction1.getId());
        assertThat(paiementSanction1).isEqualTo(paiementSanction2);
        paiementSanction2.setId(2L);
        assertThat(paiementSanction1).isNotEqualTo(paiementSanction2);
        paiementSanction1.setId(null);
        assertThat(paiementSanction1).isNotEqualTo(paiementSanction2);
    }
}
