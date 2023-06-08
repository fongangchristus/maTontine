package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GestionnaireBanqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GestionnaireBanque.class);
        GestionnaireBanque gestionnaireBanque1 = new GestionnaireBanque();
        gestionnaireBanque1.setId(1L);
        GestionnaireBanque gestionnaireBanque2 = new GestionnaireBanque();
        gestionnaireBanque2.setId(gestionnaireBanque1.getId());
        assertThat(gestionnaireBanque1).isEqualTo(gestionnaireBanque2);
        gestionnaireBanque2.setId(2L);
        assertThat(gestionnaireBanque1).isNotEqualTo(gestionnaireBanque2);
        gestionnaireBanque1.setId(null);
        assertThat(gestionnaireBanque1).isNotEqualTo(gestionnaireBanque2);
    }
}
