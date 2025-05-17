package com.example.scrapetok.repository;

import com.example.scrapetok.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount,Long> {
    Optional<AdminAccount> findByEmail(String email);
}
