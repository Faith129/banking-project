package com.orbit.repository;

import com.orbit.models.BalanceEnquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEnquiry, Long> {
    Optional<BalanceEnquiry> findByAccountNo(String accountNo);
}
