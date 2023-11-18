package com.example.trixiapp.service;

import com.example.trixiapp.model.Municipality;
import com.example.trixiapp.model.PartOfMunicipality;
import com.example.trixiapp.repository.PartOfMunicipalityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PartOfMunicipalityService {
    private final PartOfMunicipalityRepository partOfMunicipalityRepository;

    public void createPartOfMunicipality(PartOfMunicipality partOfMunicipality) {
        partOfMunicipalityRepository.save(partOfMunicipality);
    }
}
