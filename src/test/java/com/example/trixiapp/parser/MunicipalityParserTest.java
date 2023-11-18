package com.example.trixiapp.parser;

import com.example.trixiapp.model.Municipality;
import com.example.trixiapp.model.PartOfMunicipality;
import com.example.trixiapp.service.MunicipalityService;
import com.example.trixiapp.service.PartOfMunicipalityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MunicipalityParserTest {
    @InjectMocks
    MunicipalityParser municipalityParser;

    @Mock
    MunicipalityService municipalityService;

    @Mock
    PartOfMunicipalityService partOfMunicipalityService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void parseMunicipality() {
        String filepath = "src" + File.separator +
                "test" + File.separator +
                "resources" + File.separator +
                "testfile.xml" + File.separator;
        List<Municipality> municipalities= municipalityParser.parseMunicipality(filepath);

        Municipality parsedMunicipality = municipalities.get(0);

        verify(municipalityService).createMunicipality(parsedMunicipality);
        assertEquals(1,municipalities.size());
        assertEquals(573060, parsedMunicipality.getId());
        assertEquals("Kopidlno", parsedMunicipality.getName());

    }

    @Test
    void parseMunicipalityInvalidXml() {
        String filepath = "Invalid path";

        assertThrows(RuntimeException.class, () ->  municipalityParser.parseMunicipality(filepath));
    }

    @Test
    void parsePartOfMunicipality() {
        String filepath = "src" + File.separator +
                "test" + File.separator +
                "resources" + File.separator +
                "testfile.xml" + File.separator;


        List<PartOfMunicipality> partsOfMunicipality= municipalityParser.parsePartOfMunicipality(filepath);

        PartOfMunicipality parsedPartOfMunicipality1 = partsOfMunicipality.get(0);
        PartOfMunicipality parsedPartOfMunicipality2 = partsOfMunicipality.get(1);

        assertEquals(2,partsOfMunicipality.size());

        verify(partOfMunicipalityService).createPartOfMunicipality(parsedPartOfMunicipality1);
        assertEquals(69299, parsedPartOfMunicipality1.getId());
        assertEquals("Kopidlno", parsedPartOfMunicipality1 .getName());
        assertEquals(573060, parsedPartOfMunicipality1.getMunicipalityId());

        verify(partOfMunicipalityService).createPartOfMunicipality(parsedPartOfMunicipality2);
        assertEquals(69302, parsedPartOfMunicipality2.getId());
        assertEquals("Ledkov", parsedPartOfMunicipality2.getName());
        assertEquals(573060, parsedPartOfMunicipality2.getMunicipalityId());

    }

    @Test
    void parsePartOfMunicipalityInvalidXml() {
        String filepath = "Invalid path";

        assertThrows(RuntimeException.class, () ->  municipalityParser.parsePartOfMunicipality(filepath));
    }

}