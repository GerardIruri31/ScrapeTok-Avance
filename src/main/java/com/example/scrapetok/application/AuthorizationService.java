package com.example.scrapetok.application;

import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpResponseDTO;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.UserApifyCallHistorial;
import com.example.scrapetok.repository.GeneralAccountRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Transactional
public class AuthorizationService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GeneralAccountRepository generalAccountRepository;

    public UserSignUpResponseDTO createUser(UserSignUpRequestDTO request) {
        GeneralAccount newUser = modelMapper.map(request, GeneralAccount.class);
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        newUser.setHistorial(new UserApifyCallHistorial());
        GeneralAccount saved = generalAccountRepository.save(newUser);
        return modelMapper.map(saved, UserSignUpResponseDTO.class);
    }

    private ZonedDateTime obtenerFechaPeru() {
        return ZonedDateTime.now(ZoneId.of("America/Lima"));
    }
}
