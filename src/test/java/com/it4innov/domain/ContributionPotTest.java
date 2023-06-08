package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContributionPotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionPot.class);
        ContributionPot contributionPot1 = new ContributionPot();
        contributionPot1.setId(1L);
        ContributionPot contributionPot2 = new ContributionPot();
        contributionPot2.setId(contributionPot1.getId());
        assertThat(contributionPot1).isEqualTo(contributionPot2);
        contributionPot2.setId(2L);
        assertThat(contributionPot1).isNotEqualTo(contributionPot2);
        contributionPot1.setId(null);
        assertThat(contributionPot1).isNotEqualTo(contributionPot2);
    }
}
