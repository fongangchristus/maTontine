package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementTontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementTontine.class);
        PaiementTontine paiementTontine1 = new PaiementTontine();
        paiementTontine1.setId(1L);
        PaiementTontine paiementTontine2 = new PaiementTontine();
        paiementTontine2.setId(paiementTontine1.getId());
        assertThat(paiementTontine1).isEqualTo(paiementTontine2);
        paiementTontine2.setId(2L);
        assertThat(paiementTontine1).isNotEqualTo(paiementTontine2);
        paiementTontine1.setId(null);
        assertThat(paiementTontine1).isNotEqualTo(paiementTontine2);
    }
}
