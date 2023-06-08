package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContributionPotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionPotDTO.class);
        ContributionPotDTO contributionPotDTO1 = new ContributionPotDTO();
        contributionPotDTO1.setId(1L);
        ContributionPotDTO contributionPotDTO2 = new ContributionPotDTO();
        assertThat(contributionPotDTO1).isNotEqualTo(contributionPotDTO2);
        contributionPotDTO2.setId(contributionPotDTO1.getId());
        assertThat(contributionPotDTO1).isEqualTo(contributionPotDTO2);
        contributionPotDTO2.setId(2L);
        assertThat(contributionPotDTO1).isNotEqualTo(contributionPotDTO2);
        contributionPotDTO1.setId(null);
        assertThat(contributionPotDTO1).isNotEqualTo(contributionPotDTO2);
    }
}
