package com.example.scrapetok.application;

import com.example.scrapetok.domain.*;
import com.example.scrapetok.domain.DTO.AdminProfileResponseDTO;
import com.example.scrapetok.domain.DTO.UserProfileResponseDTO;
import com.example.scrapetok.domain.enums.ApifyRunStatus;
import com.example.scrapetok.repository.GeneralAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserAdminProfileService {
    @Autowired
    private GeneralAccountRepository generalAccountRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UserProfileResponseDTO getUserProfile(Long userId) throws EntityNotFoundException {
        GeneralAccount user = generalAccountRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User with id " + " Not Found"));
        UserProfileResponseDTO userProfile = modelMapper.map(user, UserProfileResponseDTO.class);

        UserApifyCallHistorial userHistorial = user.getHistorial();
        if (userHistorial == null) {
            userProfile.setAmountScrappedAccount(0);
            userProfile.setFilters(new ArrayList<>());
            userProfile.setTiktokUsernameScraped(Collections.emptySet());
            throw new EntityNotFoundException("Historial not found for user " + userId);
        }

        userProfile.setAmountScrappedAccount(userHistorial.getAmountScrappedAccount() != null ? userHistorial.getAmountScrappedAccount() : 0);
        List<Map<Object,Object>> filterMatrix = new ArrayList<>();
        List<UserApifyFilters> historialFilters = userHistorial.getFiltros();
        for (UserApifyFilters filter: historialFilters) {
            if (filter.getApifyRunStatus() == ApifyRunStatus.COMPLETED) {
                Map<Object,Object> filterHashmap= new HashMap<>();
                filterHashmap.put("Hashtags", filter.getHashtags());
                filterHashmap.put("Date From", filter.getDateFrom());
                filterHashmap.put("Date to", filter.getDateTo());
                filterHashmap.put("Key Word", filter.getKeyWords());
                filterHashmap.put("N Last Post By Hashtags", filter.getNlastPostByHashtags());
                filterHashmap.put("Tiktok Usernames", filter.getTiktokAccount());
                filterHashmap.put("Execution Time", filter.getExecutionTime());
                filterMatrix.add(filterHashmap);
            }
        }
        userProfile.setFilters(filterMatrix);

        Set<String> usernames = new HashSet<>();
        Set<TiktokUsername> scrapedAccounts = userHistorial.getTiktokUsernames();
        if (scrapedAccounts != null) {
            for (TiktokUsername scrapedAccount : scrapedAccounts) {
                usernames.add(scrapedAccount.getUsername());
            }
        } else usernames = Collections.emptySet();
        userProfile.setTiktokUsernameScraped(usernames);
        return userProfile;
    }


    public AdminProfileResponseDTO getAdminProfile(Long adminId) throws EntityNotFoundException {
        GeneralAccount user = generalAccountRepository.findById(adminId).orElseThrow(()-> new EntityNotFoundException("User with id " + " Not Found"));
        AdminProfileResponseDTO userProfile = modelMapper.map(user, AdminProfileResponseDTO.class);
        AdminProfile admin = user.getAdmin();
        if (admin == null) {
            return userProfile;
        }
        modelMapper.map(admin,userProfile);

        List<Map<String,String>> questionAnswers = null;
        if (admin.getAnswers() != null) {
            questionAnswers = new ArrayList<>();
            for (QuestAndAnswer qa : admin.getAnswers()) {
                Map<String,String> map = new HashMap<>();
                map.put(qa.getQuestionDescription(), qa.getAnswerDescription());
                questionAnswers.add(map);
            }
        }
        userProfile.setQuestionAndAnswer(questionAnswers);

        List<Map<Long,String>> dailyAlerts = null;
        if (admin.getAlert() != null) {
            dailyAlerts = new ArrayList<>();
            for (DailyAlerts alert : admin.getAlert()) {
                Map<Long,String> map = new HashMap<>();
                map.put(alert.getId(), alert.getPostedDate() != null
                        ? alert.getPostedDate().toString()
                        : null);
                dailyAlerts.add(map);
            }
        }
        userProfile.setEmmitedAlerts(dailyAlerts);
        return userProfile;
    }
}
