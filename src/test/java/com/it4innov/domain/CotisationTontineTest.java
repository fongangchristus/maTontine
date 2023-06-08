package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CotisationTontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CotisationTontine.class);
        CotisationTontine cotisationTontine1 = new CotisationTontine();
        cotisationTontine1.setId(1L);
        CotisationTontine cotisationTontine2 = new CotisationTontine();
        cotisationTontine2.setId(cotisationTontine1.getId());
        assertThat(cotisationTontine1).isEqualTo(cotisationTontine2);
        cotisationTontine2.setId(2L);
        assertThat(cotisationTontine1).isNotEqualTo(cotisationTontine2);
        cotisationTontine1.setId(null);
        assertThat(cotisationTontine1).isNotEqualTo(cotisationTontine2);
    }
}
