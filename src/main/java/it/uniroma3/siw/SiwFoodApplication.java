package it.uniroma3.siw;

import it.uniroma3.siw.model.Admin;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.AdminRepository;
import it.uniroma3.siw.service.AdminService;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiwFoodApplication {



    public static void main(String[] args) {

        SpringApplication.run(SiwFoodApplication.class, args);
    }

}
