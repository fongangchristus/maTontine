package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssembleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assemble.class);
        Assemble assemble1 = new Assemble();
        assemble1.setId(1L);
        Assemble assemble2 = new Assemble();
        assemble2.setId(assemble1.getId());
        assertThat(assemble1).isEqualTo(assemble2);
        assemble2.setId(2L);
        assertThat(assemble1).isNotEqualTo(assemble2);
        assemble1.setId(null);
        assertThat(assemble1).isNotEqualTo(assemble2);
    }
}
