package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanctionConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SanctionConfiguration.class);
        SanctionConfiguration sanctionConfiguration1 = new SanctionConfiguration();
        sanctionConfiguration1.setId(1L);
        SanctionConfiguration sanctionConfiguration2 = new SanctionConfiguration();
        sanctionConfiguration2.setId(sanctionConfiguration1.getId());
        assertThat(sanctionConfiguration1).isEqualTo(sanctionConfiguration2);
        sanctionConfiguration2.setId(2L);
        assertThat(sanctionConfiguration1).isNotEqualTo(sanctionConfiguration2);
        sanctionConfiguration1.setId(null);
        assertThat(sanctionConfiguration1).isNotEqualTo(sanctionConfiguration2);
    }
}
