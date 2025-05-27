package com.example.scrapetok.application;

import com.example.scrapetok.domain.AdminProfile;
import com.example.scrapetok.domain.DTO.*;
import com.example.scrapetok.domain.GeneralAccount;
import com.example.scrapetok.domain.UserApifyCallHistorial;
import com.example.scrapetok.domain.enums.Role;
import com.example.scrapetok.exception.EmailAlreadyInUseException;
import com.example.scrapetok.exception.ResourceNotFoundException;
import com.example.scrapetok.repository.AdminProfileRepository;
import com.example.scrapetok.repository.GeneralAccountRepository;
import com.example.scrapetok.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;
    public UserSignUpResponseDTO createUser(UserSignUpRequestDTO request) {
        GeneralAccount newUser = modelMapper.map(request, GeneralAccount.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        ZonedDateTime zonedDateTime = obtenerFechaPeru();
        newUser.setCreationDate(zonedDateTime.toLocalDate());
        UserApifyCallHistorial historial = new UserApifyCallHistorial();
        historial.setUser(newUser);
        newUser.setHistorial(historial);
        try {
            GeneralAccount saved = generalAccountRepository.save(newUser);
            return modelMapper.map(saved, UserSignUpResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }
    }



    public AdminSystemResponseDTO createAdmin(UserSignUpRequestDTO request) {
        GeneralAccount newUser = modelMapper.map(request, GeneralAccount.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
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
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No role found"))
                .getAuthority()
                .replace("ROLE_", "");

        String token = jwtUtil.generateToken(userDetails.getUsername(), role);
        return new LoginResponseDTO(token, role);
    }
    private ZonedDateTime obtenerFechaPeru() {
        return ZonedDateTime.now(ZoneId.of("America/Lima"));
    }
}
