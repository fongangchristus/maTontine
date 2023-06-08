package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FonctionAdherentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FonctionAdherent.class);
        FonctionAdherent fonctionAdherent1 = new FonctionAdherent();
        fonctionAdherent1.setId(1L);
        FonctionAdherent fonctionAdherent2 = new FonctionAdherent();
        fonctionAdherent2.setId(fonctionAdherent1.getId());
        assertThat(fonctionAdherent1).isEqualTo(fonctionAdherent2);
        fonctionAdherent2.setId(2L);
        assertThat(fonctionAdherent1).isNotEqualTo(fonctionAdherent2);
        fonctionAdherent1.setId(null);
        assertThat(fonctionAdherent1).isNotEqualTo(fonctionAdherent2);
    }
}
