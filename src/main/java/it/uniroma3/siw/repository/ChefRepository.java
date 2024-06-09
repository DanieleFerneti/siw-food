package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Chef;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface ChefRepository extends CrudRepository<Chef,Long> {
    public boolean existsByNameAndSurnameAndDataAndImageFileName(String name, String surname, LocalDate data, String imageFileName);


    Chef findByNameAndSurname(String name, String surname);
}

