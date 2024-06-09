package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.AdminValidator;
import it.uniroma3.siw.controller.validator.ChefValidator;
import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.model.Admin;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.presentation.FileStorer;
import it.uniroma3.siw.service.AdminService;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private ChefService chefService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ChefValidator chefValidator;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @GetMapping(value = "/register")
    public String showRegisterForm (Model model, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        model.addAttribute("user", new Chef());
        model.addAttribute("credentials", new Credentials());
        return "formRegisterUser";
    }

    @GetMapping("/login")
    public String showLoginForm () {
        return "formLogin";
    }

    @GetMapping(value = "/")
    public String index(Model model) {

        model.addAttribute("chefs", this.chefService.getAllChef());
        model.addAttribute("recipes", this.recipeService.getAllRecipes());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "index";
        }
        else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "administration";
            }
            if (credentials.getRole().equals(Credentials.CHEF_ROLE)) {
                Long chef_id= credentials.getChef().getId();
                model.addAttribute("chef_id", chef_id);
                return "chef_administration";
            }
        }
        return "index";
    }

    @GetMapping("/success")
    public String defaultAfterLogin(Model model) {

        UserDetails adminDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(adminDetails.getUsername());
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "administration";
        }
        if (credentials.getRole().equals(Credentials.CHEF_ROLE)){
            Long chef_id= credentials.getChef().getId();
            model.addAttribute("chef_id", chef_id);
            return "chef_administration";
        }
        return "formLogin";
    }
/*
    @GetMapping("/success/oauth")
    public String defaultAfterLoginOauth(Model model) {

        OAuth2User userDetails = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getAttributes().toString());
        String fullName = (String) userDetails.getAttribute("name"); // Recupera il nome completo dell'utente
        Chef chef = chefService.findByFullName(fullName); // Cerca un chef con il nome completo dell'utente
        if (chef == null) {
            // Se non esiste un chef con quel nome completo, crea un nuovo chef
            chef = new Chef();
            chef.setName((String) userDetails.getAttribute("given_name"));
            chef.setSurname((String) userDetails.getAttribute("family_name"));
            chef.setData(LocalDate.now());
            // Assicurati di impostare il path dell'immagine in base ai tuoi requisiti
            chef.setimageFileName("path/to/default/image"); // Imposta il percorso predefinito dell'immagine
            chefService.save(chef);
        }

        return "redirect:/";
    }*/



    @PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") Chef chef,
                               BindingResult chefBindingResult, @Valid
                               @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,@RequestParam("file") MultipartFile file, Model model) {


        this.chefValidator.validate(chef,chefBindingResult);
        this.credentialsValidator.validate(credentials,credentialsBindingResult);
        if (!chefBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            this.chefService.save(chef); // Save chef first to get the ID
            String defaultPath = "/images/"+ chef.getDirectoryName() + "/";

            if(!file.isEmpty())
                chef.setImageFileName( defaultPath + FileStorer.store(file, chef.getDirectoryName()));
            else
                chef.setImageFileName( "/images/" + FileStorer.store(file,"images/default.jpeg"));

            // Save the chef again with the updated imageFileName
            this.chefService.save(chef);

            credentials.setChef(chef);
            credentialsService.saveCredentials(credentials);


            model.addAttribute("user", chef);
            return "registrationSuccessful";
        }
        return "formRegisterUser";
    }



    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }


}
