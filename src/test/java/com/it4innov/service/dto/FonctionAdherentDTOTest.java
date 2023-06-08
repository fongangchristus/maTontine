package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FonctionAdherentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FonctionAdherentDTO.class);
        FonctionAdherentDTO fonctionAdherentDTO1 = new FonctionAdherentDTO();
        fonctionAdherentDTO1.setId(1L);
        FonctionAdherentDTO fonctionAdherentDTO2 = new FonctionAdherentDTO();
        assertThat(fonctionAdherentDTO1).isNotEqualTo(fonctionAdherentDTO2);
        fonctionAdherentDTO2.setId(fonctionAdherentDTO1.getId());
        assertThat(fonctionAdherentDTO1).isEqualTo(fonctionAdherentDTO2);
        fonctionAdherentDTO2.setId(2L);
        assertThat(fonctionAdherentDTO1).isNotEqualTo(fonctionAdherentDTO2);
        fonctionAdherentDTO1.setId(null);
        assertThat(fonctionAdherentDTO1).isNotEqualTo(fonctionAdherentDTO2);
    }
}
