package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GestionnaireTontineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GestionnaireTontine.class);
        GestionnaireTontine gestionnaireTontine1 = new GestionnaireTontine();
        gestionnaireTontine1.setId(1L);
        GestionnaireTontine gestionnaireTontine2 = new GestionnaireTontine();
        gestionnaireTontine2.setId(gestionnaireTontine1.getId());
        assertThat(gestionnaireTontine1).isEqualTo(gestionnaireTontine2);
        gestionnaireTontine2.setId(2L);
        assertThat(gestionnaireTontine1).isNotEqualTo(gestionnaireTontine2);
        gestionnaireTontine1.setId(null);
        assertThat(gestionnaireTontine1).isNotEqualTo(gestionnaireTontine2);
    }
}
