package com.example.scrapetok.repository;

import com.example.scrapetok.domain.UserApifyCallHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserApifyCallHistorialRepository extends JpaRepository<UserApifyCallHistorial, Long> {
}
