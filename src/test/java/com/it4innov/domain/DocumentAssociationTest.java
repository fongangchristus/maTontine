package com.it4innov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentAssociationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentAssociation.class);
        DocumentAssociation documentAssociation1 = new DocumentAssociation();
        documentAssociation1.setId(1L);
        DocumentAssociation documentAssociation2 = new DocumentAssociation();
        documentAssociation2.setId(documentAssociation1.getId());
        assertThat(documentAssociation1).isEqualTo(documentAssociation2);
        documentAssociation2.setId(2L);
        assertThat(documentAssociation1).isNotEqualTo(documentAssociation2);
        documentAssociation1.setId(null);
        assertThat(documentAssociation1).isNotEqualTo(documentAssociation2);
    }
}
