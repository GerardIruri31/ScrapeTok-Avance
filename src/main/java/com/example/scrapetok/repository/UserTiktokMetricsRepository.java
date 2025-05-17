package com.example.scrapetok.repository;

import com.example.scrapetok.domain.UserTiktokMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTiktokMetricsRepository extends JpaRepository<UserTiktokMetrics,Long> {

}
