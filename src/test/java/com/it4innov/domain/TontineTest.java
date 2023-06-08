package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tontine.class);
        Tontine tontine1 = new Tontine();
        tontine1.setId(1L);
        Tontine tontine2 = new Tontine();
        tontine2.setId(tontine1.getId());
        assertThat(tontine1).isEqualTo(tontine2);
        tontine2.setId(2L);
        assertThat(tontine1).isNotEqualTo(tontine2);
        tontine1.setId(null);
        assertThat(tontine1).isNotEqualTo(tontine2);
    }
}
