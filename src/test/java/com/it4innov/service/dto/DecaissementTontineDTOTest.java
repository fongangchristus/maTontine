package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DecaissementTontineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecaissementTontineDTO.class);
        DecaissementTontineDTO decaissementTontineDTO1 = new DecaissementTontineDTO();
        decaissementTontineDTO1.setId(1L);
        DecaissementTontineDTO decaissementTontineDTO2 = new DecaissementTontineDTO();
        assertThat(decaissementTontineDTO1).isNotEqualTo(decaissementTontineDTO2);
        decaissementTontineDTO2.setId(decaissementTontineDTO1.getId());
        assertThat(decaissementTontineDTO1).isEqualTo(decaissementTontineDTO2);
        decaissementTontineDTO2.setId(2L);
        assertThat(decaissementTontineDTO1).isNotEqualTo(decaissementTontineDTO2);
        decaissementTontineDTO1.setId(null);
        assertThat(decaissementTontineDTO1).isNotEqualTo(decaissementTontineDTO2);
    }
}
