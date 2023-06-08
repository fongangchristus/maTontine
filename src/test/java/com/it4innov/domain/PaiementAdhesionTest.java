package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementAdhesionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementAdhesion.class);
        PaiementAdhesion paiementAdhesion1 = new PaiementAdhesion();
        paiementAdhesion1.setId(1L);
        PaiementAdhesion paiementAdhesion2 = new PaiementAdhesion();
        paiementAdhesion2.setId(paiementAdhesion1.getId());
        assertThat(paiementAdhesion1).isEqualTo(paiementAdhesion2);
        paiementAdhesion2.setId(2L);
        assertThat(paiementAdhesion1).isNotEqualTo(paiementAdhesion2);
        paiementAdhesion1.setId(null);
        assertThat(paiementAdhesion1).isNotEqualTo(paiementAdhesion2);
    }
}
