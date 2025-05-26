package com.example.scrapetok.application;

import com.example.scrapetok.domain.AdminProfile;
import com.example.scrapetok.domain.DTO.*;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.UserApifyCallHistorial;
import com.example.scrapetok.domain.enums.Role;
import com.example.scrapetok.repository.AdminProfileRepository;
import com.example.scrapetok.repository.GeneralAccountRepository;
import com.example.scrapetok.exception.ResourceNotFoundException;
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

    public UserSignUpResponseDTO createUser(UserSignUpRequestDTO request) throws ResourceNotFoundException, IllegalArgumentException {
        GeneralAccount newUser = modelMapper.map(request, GeneralAccount.class);
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        UserApifyCallHistorial historial = new UserApifyCallHistorial();
        historial.setUser(newUser);
        newUser.setHistorial(historial);
        GeneralAccount saved = generalAccountRepository.save(newUser);
        return modelMapper.map(saved, UserSignUpResponseDTO.class);
    }



    public AdminSystemResponseDTO createAdmin(UserSignUpRequestDTO request) {
        GeneralAccount newUser = modelMapper.map(request, GeneralAccount.class);
        newUser.setRole(Role.ADMIN);
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        UserApifyCallHistorial historial = new UserApifyCallHistorial();
        historial.setUser(newUser);
        newUser.setHistorial(historial);


        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setUser(newUser);
        adminProfile.setAdmisionToAdminDate(zonedDateTime.toLocalDate());
        adminProfile.setAdmisionToAdminTime(zonedDateTime.toLocalTime());
        adminProfile.setIsActive(true);
        adminProfile.setTotalQuestionsAnswered(0);
        GeneralAccount savedUser = generalAccountRepository.save(newUser);
        AdminProfile savedAdmin = adminProfileRepository.save(adminProfile);
        AdminSystemResponseDTO requestDTO = modelMapper.map(savedUser, AdminSystemResponseDTO.class);
        modelMapper.map(savedAdmin,requestDTO);
        return requestDTO;
    }


    public UpgradeToAdminResponseDTO upgrade(UpgradeToAdminRequestDTO request) {
        // Verifica que el admin que está promoviendo exista
        AdminProfile admin = adminProfileRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin with id " + request.getAdminId() + " not found"));

        // Verifica que el usuario a promover exista
        GeneralAccount user = generalAccountRepository.findById(request.getUserid())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.getUserid() + " not found"));

        if (user.getRole() == Role.ADMIN)
            throw new IllegalStateException("⚠️ This user is already an admin.");

        // Crear nuevo perfil de admin
        AdminProfile nuevoAdmin = new AdminProfile();
        nuevoAdmin.setUser(user);

        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        nuevoAdmin.setAdmisionToAdminDate(zonedDateTime.toLocalDate());
        nuevoAdmin.setAdmisionToAdminTime(zonedDateTime.toLocalTime());

        user.setRole(Role.ADMIN);

        adminProfileRepository.save(nuevoAdmin);
        generalAccountRepository.save(user);

        UpgradeToAdminResponseDTO responseDTO = new UpgradeToAdminResponseDTO();
        modelMapper.map(user, responseDTO);
        modelMapper.map(nuevoAdmin, responseDTO);
        return responseDTO;
    }

    private ZonedDateTime obtenerFechaPeru() {
        return ZonedDateTime.now(ZoneId.of("America/Lima"));
    }
}
