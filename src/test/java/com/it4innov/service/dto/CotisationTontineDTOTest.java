package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CotisationTontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CotisationTontineDTO.class);
        CotisationTontineDTO cotisationTontineDTO1 = new CotisationTontineDTO();
        cotisationTontineDTO1.setId(1L);
        CotisationTontineDTO cotisationTontineDTO2 = new CotisationTontineDTO();
        assertThat(cotisationTontineDTO1).isNotEqualTo(cotisationTontineDTO2);
        cotisationTontineDTO2.setId(cotisationTontineDTO1.getId());
        assertThat(cotisationTontineDTO1).isEqualTo(cotisationTontineDTO2);
        cotisationTontineDTO2.setId(2L);
        assertThat(cotisationTontineDTO1).isNotEqualTo(cotisationTontineDTO2);
        cotisationTontineDTO1.setId(null);
        assertThat(cotisationTontineDTO1).isNotEqualTo(cotisationTontineDTO2);
    }
}
