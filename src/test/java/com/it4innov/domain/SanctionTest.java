package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanctionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sanction.class);
        Sanction sanction1 = new Sanction();
        sanction1.setId(1L);
        Sanction sanction2 = new Sanction();
        sanction2.setId(sanction1.getId());
        assertThat(sanction1).isEqualTo(sanction2);
        sanction2.setId(2L);
        assertThat(sanction1).isNotEqualTo(sanction2);
        sanction1.setId(null);
        assertThat(sanction1).isNotEqualTo(sanction2);
    }
}
