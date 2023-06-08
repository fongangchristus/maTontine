package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DecaissementTontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecaissementTontine.class);
        DecaissementTontine decaissementTontine1 = new DecaissementTontine();
        decaissementTontine1.setId(1L);
        DecaissementTontine decaissementTontine2 = new DecaissementTontine();
        decaissementTontine2.setId(decaissementTontine1.getId());
        assertThat(decaissementTontine1).isEqualTo(decaissementTontine2);
        decaissementTontine2.setId(2L);
        assertThat(decaissementTontine1).isNotEqualTo(decaissementTontine2);
        decaissementTontine1.setId(null);
        assertThat(decaissementTontine1).isNotEqualTo(decaissementTontine2);
    }
}
