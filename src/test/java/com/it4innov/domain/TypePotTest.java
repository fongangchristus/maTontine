package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePot.class);
        TypePot typePot1 = new TypePot();
        typePot1.setId(1L);
        TypePot typePot2 = new TypePot();
        typePot2.setId(typePot1.getId());
        assertThat(typePot1).isEqualTo(typePot2);
        typePot2.setId(2L);
        assertThat(typePot1).isNotEqualTo(typePot2);
        typePot1.setId(null);
        assertThat(typePot1).isNotEqualTo(typePot2);
    }
}
