package com.example.trixiapp.parser;

import com.example.trixiapp.model.Municipality;
import com.example.trixiapp.model.PartOfMunicipality;
import com.example.trixiapp.service.MunicipalityService;
import com.example.trixiapp.service.PartOfMunicipalityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MunicipalityParser {

    private final MunicipalityService municipalityService;
    private final PartOfMunicipalityService partOfMunicipalityService;

    public List<Municipality> parseMunicipality(String filePath) {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);

            List<Municipality> parsedMunicipalities = new ArrayList<>();
            NodeList municipalityNodes = doc.getElementsByTagName("vf:Obec");
            for (int i = 0; i < municipalityNodes.getLength(); i++) {
                Element municipalityElement = (Element) municipalityNodes.item(i);

                String municipalityId = municipalityElement.getElementsByTagName("obi:Kod").item(0).getTextContent();
                String municipalityName = municipalityElement.getElementsByTagName("obi:Nazev").item(0).getTextContent();
                Municipality municipality = new Municipality(Long.valueOf(municipalityId),municipalityName);
                parsedMunicipalities.add(municipality);
                municipalityService.createMunicipality(municipality);
                System.out.println("Parsed Municipality: " + municipalityId + " "+ municipalityName);

            } return parsedMunicipalities;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }


    }
    public List<PartOfMunicipality> parsePartOfMunicipality(String filePath) {

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);

            List<PartOfMunicipality> parsedPartsOfMunicipality = new ArrayList<>();
            NodeList municipalities = doc.getElementsByTagName("vf:CastObce");
            for (int i = 0; i < municipalities.getLength(); i++) {
                Element partOfMunicipalityElement = (Element) municipalities.item(i);

                String partOfMunicipalityId = partOfMunicipalityElement.getElementsByTagName("coi:Kod").item(0).getTextContent();
                String partOfMunicipalityName = partOfMunicipalityElement.getElementsByTagName("coi:Nazev")
                        .item(0).getTextContent();

                Element municipalityElement = (Element) partOfMunicipalityElement.getElementsByTagName("coi:Obec").item(0);
                String municipalityId = municipalityElement.getElementsByTagName("obi:Kod").item(0).getTextContent();

                PartOfMunicipality partOfMunicipality = new PartOfMunicipality(
                        Long.valueOf(partOfMunicipalityId),
                        partOfMunicipalityName,
                        Long.valueOf(municipalityId));

                parsedPartsOfMunicipality.add(partOfMunicipality);
                partOfMunicipalityService.createPartOfMunicipality(partOfMunicipality);
                System.out.println("Parsed Part of Municipality: " +
                        partOfMunicipalityId + " " +
                        partOfMunicipalityName + " " +
                        municipalityId);
            }
            return parsedPartsOfMunicipality;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }


    }

}
