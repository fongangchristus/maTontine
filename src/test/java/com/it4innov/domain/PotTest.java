package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pot.class);
        Pot pot1 = new Pot();
        pot1.setId(1L);
        Pot pot2 = new Pot();
        pot2.setId(pot1.getId());
        assertThat(pot1).isEqualTo(pot2);
        pot2.setId(2L);
        assertThat(pot1).isNotEqualTo(pot2);
        pot1.setId(null);
        assertThat(pot1).isNotEqualTo(pot2);
    }
}
