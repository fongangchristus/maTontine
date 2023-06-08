package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteRIBTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteRIB.class);
        CompteRIB compteRIB1 = new CompteRIB();
        compteRIB1.setId(1L);
        CompteRIB compteRIB2 = new CompteRIB();
        compteRIB2.setId(compteRIB1.getId());
        assertThat(compteRIB1).isEqualTo(compteRIB2);
        compteRIB2.setId(2L);
        assertThat(compteRIB1).isNotEqualTo(compteRIB2);
        compteRIB1.setId(null);
        assertThat(compteRIB1).isNotEqualTo(compteRIB2);
    }
}
