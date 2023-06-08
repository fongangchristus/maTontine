package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanctionConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SanctionConfigurationDTO.class);
        SanctionConfigurationDTO sanctionConfigurationDTO1 = new SanctionConfigurationDTO();
        sanctionConfigurationDTO1.setId(1L);
        SanctionConfigurationDTO sanctionConfigurationDTO2 = new SanctionConfigurationDTO();
        assertThat(sanctionConfigurationDTO1).isNotEqualTo(sanctionConfigurationDTO2);
        sanctionConfigurationDTO2.setId(sanctionConfigurationDTO1.getId());
        assertThat(sanctionConfigurationDTO1).isEqualTo(sanctionConfigurationDTO2);
        sanctionConfigurationDTO2.setId(2L);
        assertThat(sanctionConfigurationDTO1).isNotEqualTo(sanctionConfigurationDTO2);
        sanctionConfigurationDTO1.setId(null);
        assertThat(sanctionConfigurationDTO1).isNotEqualTo(sanctionConfigurationDTO2);
    }
}
