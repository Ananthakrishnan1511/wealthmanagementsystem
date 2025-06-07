package com.cts.wealthmanagementsystem.repository;

import com.cts.wealthmanagementsystem.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByEmailAddress(String emailAddress);

    Optional<Client> findByEmailAddressAndPassWord(String emailAddress, String passWord);

    List<Client> findByIsApprovedFalse();

    List<Client> findByIsApprovedTrue();

    boolean existsByEmailAddress(String emailAddress); // ✅ Corrected method name
}
