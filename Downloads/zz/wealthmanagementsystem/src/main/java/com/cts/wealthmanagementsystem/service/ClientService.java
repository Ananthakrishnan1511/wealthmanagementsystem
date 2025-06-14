package com.cts.wealthmanagementsystem.service;

import com.cts.wealthmanagementsystem.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    boolean existsByEmail(String emailAddress);

    Client addClient(Client newClient);

    Optional<Client> getClientByEmail(String emailAddress);

    List<Client> getAllDraftUsers();

    void approveUser(Integer identity);

    void rejectUser(Integer identity);

    List<Client> getApprovedClients();
}
