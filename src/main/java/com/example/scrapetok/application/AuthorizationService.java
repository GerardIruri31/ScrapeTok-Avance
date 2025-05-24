package com.example.scrapetok.application;

import com.example.scrapetok.domain.AdminProfile;
import com.example.scrapetok.domain.DTO.UpgradeToAdminResponseDTO;
import com.example.scrapetok.domain.DTO.UpgradeToAdminRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpRequestDTO;
import com.example.scrapetok.domain.DTO.UserSignUpResponseDTO;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.UserApifyCallHistorial;
import com.example.scrapetok.domain.enums.Role;
import com.example.scrapetok.repository.AdminProfileRepository;
import com.example.scrapetok.repository.GeneralAccountRepository;
import jakarta.persistence.EntityNotFoundException;
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
    @Autowired
    private AdminProfileRepository adminProfileRepository;

    public UserSignUpResponseDTO createUser(UserSignUpRequestDTO request) throws EntityNotFoundException, IllegalArgumentException {
        GeneralAccount newUser = modelMapper.map(request, GeneralAccount.class);
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        newUser.setHistorial(new UserApifyCallHistorial());
        GeneralAccount saved = generalAccountRepository.save(newUser);
        return modelMapper.map(saved, UserSignUpResponseDTO.class);
    }

    public UpgradeToAdminResponseDTO upgrade(UpgradeToAdminRequestDTO request) {
        // Temporal -> implementar seguridad
        AdminProfile admin = adminProfileRepository.findById(request.getAdminId()).orElseThrow(() -> new EntityNotFoundException("Admin with id " + request.getAdminId() + " Not found"));
        GeneralAccount user = generalAccountRepository.findById(request.getAdminId()).orElseThrow(() -> new EntityNotFoundException("User with id " + request.getAdminId() + "Not Found"));
        if (user.getRole() == Role.ADMIN) throw new IllegalStateException("⚠️ This user is already an admin.");

        AdminProfile nuevoAdmin = new AdminProfile();
        nuevoAdmin.setUser(user);
        user.setRole(Role.ADMIN);
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        nuevoAdmin.setAdmisionToAdminDate(zonedDateTime.toLocalDate());
        nuevoAdmin.setAdmisionToAdminTime(zonedDateTime.toLocalTime());

        AdminProfile savedAdmin = adminProfileRepository.save(nuevoAdmin);
        GeneralAccount savedUser = generalAccountRepository.save(user);

        UpgradeToAdminResponseDTO responseDTO = new UpgradeToAdminResponseDTO();
        modelMapper.map(savedUser, responseDTO);
        modelMapper.map(savedAdmin, responseDTO);
        return responseDTO;
    }

    private ZonedDateTime obtenerFechaPeru() {
        return ZonedDateTime.now(ZoneId.of("America/Lima"));
    }
}
