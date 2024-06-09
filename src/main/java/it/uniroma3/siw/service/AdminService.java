package it.uniroma3.siw.service;


import it.uniroma3.siw.model.Admin;
import it.uniroma3.siw.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminService {

	@Autowired
    protected AdminRepository adminRepository;

    public Admin getAdmin(Long id) {
        Optional<Admin> result = this.adminRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Admin saveAdmin(Admin admin) {
        return this.adminRepository.save(admin);
    }

    
}
