package com.cts.wealthmanagementsystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.repository.ClientRepository;

@Service
public class ClientServiceImplementation implements ClientService {
	@Autowired
	public ClientRepository clientRepository;
	@Autowired
    public PasswordEncoder passwordEncoder;
	

	 public ClientServiceImplementation(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
	     this.clientRepository = clientRepository;
	     this.passwordEncoder = passwordEncoder;
	 }

    @Override
    public Client addClient(Client newClient) {
        System.out.println("🔹 Saving Client: " + newClient.getEmailAddress());
        newClient.setPassWord(passwordEncoder.encode(newClient.getPassWord())); // ✅ Encrypt before saving
        return clientRepository.save(newClient);
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmailAddress(email);
    }

    @Override
    public List<Client> getAllDraftUsers() {
        return clientRepository.findByIsApprovedFalse();
    }

    @Override
    public void approveUser(Integer identity) {
        Optional<Client> advisor = clientRepository.findById(identity);
        advisor.ifPresent(user -> {
            user.setApproved(true); 
            clientRepository.save(user);
        });
    }

    @Override
    public void rejectUser(Integer identity) {
    	clientRepository.deleteById(identity);
    }
    public List<Client> getApprovedClients() {
        return clientRepository.findByIsApprovedTrue(); 
    }

	@Override
	public boolean existsByEmail(String email) {
		return clientRepository.existsByEmailAddress(email);
	}

	
	
}   



