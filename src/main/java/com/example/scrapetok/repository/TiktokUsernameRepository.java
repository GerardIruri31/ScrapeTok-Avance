package com.example.scrapetok.repository;

import com.example.scrapetok.domain.TiktokUsername;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TiktokUsernameRepository extends JpaRepository<TiktokUsername,Long> {
}
