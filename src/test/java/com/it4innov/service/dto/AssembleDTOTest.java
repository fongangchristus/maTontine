package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssembleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssembleDTO.class);
        AssembleDTO assembleDTO1 = new AssembleDTO();
        assembleDTO1.setId(1L);
        AssembleDTO assembleDTO2 = new AssembleDTO();
        assertThat(assembleDTO1).isNotEqualTo(assembleDTO2);
        assembleDTO2.setId(assembleDTO1.getId());
        assertThat(assembleDTO1).isEqualTo(assembleDTO2);
        assembleDTO2.setId(2L);
        assertThat(assembleDTO1).isNotEqualTo(assembleDTO2);
        assembleDTO1.setId(null);
        assertThat(assembleDTO1).isNotEqualTo(assembleDTO2);
    }
}
