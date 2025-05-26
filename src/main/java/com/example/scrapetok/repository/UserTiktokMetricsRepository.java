package com.example.scrapetok.repository;

import com.example.scrapetok.domain.UserTiktokMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface UserTiktokMetricsRepository extends JpaRepository<UserTiktokMetrics,Long> {
    List<UserTiktokMetrics> findUsernameTiktokAccountByUserId(Long userId);;
}
