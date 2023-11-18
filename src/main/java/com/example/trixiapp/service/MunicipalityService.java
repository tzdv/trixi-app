package com.example.trixiapp.service;

import com.example.trixiapp.model.Municipality;
import com.example.trixiapp.repository.MunicipalityRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;

    public void createMunicipality(Municipality municipality) {
        municipalityRepository.save(municipality);
    }

}
