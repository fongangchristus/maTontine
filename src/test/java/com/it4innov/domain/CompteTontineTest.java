package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteTontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteTontine.class);
        CompteTontine compteTontine1 = new CompteTontine();
        compteTontine1.setId(1L);
        CompteTontine compteTontine2 = new CompteTontine();
        compteTontine2.setId(compteTontine1.getId());
        assertThat(compteTontine1).isEqualTo(compteTontine2);
        compteTontine2.setId(2L);
        assertThat(compteTontine1).isNotEqualTo(compteTontine2);
        compteTontine1.setId(null);
        assertThat(compteTontine1).isNotEqualTo(compteTontine2);
    }
}
