package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChefService {
    @Autowired
    private ChefRepository chefRepository;

    @Transactional
    public void save(Chef chef) {
        this.chefRepository.save(chef);
    }


    @Transactional
    public Chef getChef(Long id) {
        Optional<Chef> result = this.chefRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public List<Chef> getAllChef() {
        List<Chef> chef = new ArrayList<Chef>();
        for(Chef c : this.chefRepository.findAll()) {
            chef.add(c);
        }
        return chef;
    }

    @Transactional
    public boolean alreadyExists(Chef chef) {
        return this.chefRepository.existsByNameAndSurnameAndDataAndImageFileName(chef.getName(),
                chef.getSurname(), chef.getData(),chef.getImageFileName());
    }

    @Transactional
    public void deleteById(Long id) {
        this.chefRepository.deleteById(id);
    }

    @Transactional
    public Chef findByFullName(String fullName) {
        // Supponendo che il nome completo sia composto da nome e cognome separati da uno spazio
        String[] parts = fullName.split(" ");
        if (parts.length != 2) {
            // Se il formato del nome non è valido (ad esempio, non c'è uno spazio per separare il nome e il cognome), restituisci null
            return null;
        }

        // Estrai il nome e il cognome dai componenti divisi
        String firstName = parts[0];
        String lastName = parts[1];

        // Cerca lo chef nel repository per nome e cognome
        return chefRepository.findByNameAndSurname(firstName, lastName);
    }
}

