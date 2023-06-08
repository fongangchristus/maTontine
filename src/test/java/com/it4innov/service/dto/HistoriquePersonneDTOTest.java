package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriquePersonneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriquePersonneDTO.class);
        HistoriquePersonneDTO historiquePersonneDTO1 = new HistoriquePersonneDTO();
        historiquePersonneDTO1.setId(1L);
        HistoriquePersonneDTO historiquePersonneDTO2 = new HistoriquePersonneDTO();
        assertThat(historiquePersonneDTO1).isNotEqualTo(historiquePersonneDTO2);
        historiquePersonneDTO2.setId(historiquePersonneDTO1.getId());
        assertThat(historiquePersonneDTO1).isEqualTo(historiquePersonneDTO2);
        historiquePersonneDTO2.setId(2L);
        assertThat(historiquePersonneDTO1).isNotEqualTo(historiquePersonneDTO2);
        historiquePersonneDTO1.setId(null);
        assertThat(historiquePersonneDTO1).isNotEqualTo(historiquePersonneDTO2);
    }
}
