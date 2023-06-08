package com.it4innov.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.it4innov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentAssociationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentAssociationDTO.class);
        DocumentAssociationDTO documentAssociationDTO1 = new DocumentAssociationDTO();
        documentAssociationDTO1.setId(1L);
        DocumentAssociationDTO documentAssociationDTO2 = new DocumentAssociationDTO();
        assertThat(documentAssociationDTO1).isNotEqualTo(documentAssociationDTO2);
        documentAssociationDTO2.setId(documentAssociationDTO1.getId());
        assertThat(documentAssociationDTO1).isEqualTo(documentAssociationDTO2);
        documentAssociationDTO2.setId(2L);
        assertThat(documentAssociationDTO1).isNotEqualTo(documentAssociationDTO2);
        documentAssociationDTO1.setId(null);
        assertThat(documentAssociationDTO1).isNotEqualTo(documentAssociationDTO2);
    }
}
