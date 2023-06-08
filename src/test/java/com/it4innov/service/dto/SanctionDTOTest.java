package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SanctionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SanctionDTO.class);
        SanctionDTO sanctionDTO1 = new SanctionDTO();
        sanctionDTO1.setId(1L);
        SanctionDTO sanctionDTO2 = new SanctionDTO();
        assertThat(sanctionDTO1).isNotEqualTo(sanctionDTO2);
        sanctionDTO2.setId(sanctionDTO1.getId());
        assertThat(sanctionDTO1).isEqualTo(sanctionDTO2);
        sanctionDTO2.setId(2L);
        assertThat(sanctionDTO1).isNotEqualTo(sanctionDTO2);
        sanctionDTO1.setId(null);
        assertThat(sanctionDTO1).isNotEqualTo(sanctionDTO2);
    }
}
