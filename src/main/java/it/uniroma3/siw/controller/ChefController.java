package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.ChefValidator;
import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.presentation.FileStorer;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ChefController {

    @Autowired
    private ChefService chefService;

    @Autowired
    private ChefValidator chefValidator;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CredentialsService credentialService;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @GetMapping("/admin/chef_management")
    public String getAllChefs(Model model) {
        model.addAttribute("chefs", this.chefService.getAllChef());
        return "admin/chef/chef_management";
    }

    @GetMapping("/admin/chef_management/create_chef")
    public String showAddChefForm(Model model) {
        model.addAttribute("chef", new Chef());
        return "admin/chef/create_chef";
    }

    @PostMapping("/admin/chef_management/add_chef")
    public String addChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult,
                          @RequestParam("file") MultipartFile file) {


        this.chefValidator.validate(chef,bindingResult);

        if (!bindingResult.hasErrors()) {

            this.chefService.save(chef); // Save chef first to get the ID
            String defaultPath = "/images/"+ chef.getDirectoryName() + "/";

            if(!file.isEmpty())
                chef.setImageFileName( defaultPath + FileStorer.store(file, chef.getDirectoryName()));
            else
                chef.setImageFileName( "/images/" + FileStorer.store(file,"images/default.jpeg"));

            // Save the chef again with the updated imageFileName
            this.chefService.save(chef);

            Credentials credentialsNewchef = new Credentials();
            credentialsNewchef.setUsername(chef.getName()+" "+chef.getSurname());
            credentialsNewchef.setPassword("password");
            credentialsNewchef.setChef(chef);
            this.credentialService.saveCredentials(credentialsNewchef);

            return "redirect:/admin/chef_management";
        } else {
            return "admin/chef/create_chef";
        }
    }



    @GetMapping("/admin/chef_management/edit_chef/{id}")
    public String  showEditChefForm(@PathVariable Long id, Model model) {
        model.addAttribute("chef", chefService.getChef(id));
        return "admin/chef/edit_chef";
    }

    @PostMapping("/admin/chef_management/{id}")
    public String editChef(@PathVariable Long id,
                           @Valid @ModelAttribute("chef") Chef chef,
                           BindingResult bindingResult,
                           @RequestParam("file") MultipartFile file) {

        this.chefValidator.validate(chef,bindingResult);

        if (!bindingResult.hasErrors()){
            this.chefService.save(chef); // Save chef first to get the ID
            String defaultPath = "/images/"+ chef.getDirectoryName() + "/";

            if(!file.isEmpty())
                chef.setImageFileName( defaultPath + FileStorer.store(file, chef.getDirectoryName()));
            else
                chef.setImageFileName( "/images/" + FileStorer.store(file,"images/default.jpeg"));

            this.chefService.save(chef);
            return "redirect:/admin/chef_management";
        } else {
            return "admin/chef/edit_chef";
        }
    }


    @GetMapping("/admin/chef_management/delete_chef/{id}")
    public String deleteChef(@PathVariable Long id) {
        // Recupera lo chef dall'ID
        Chef chef = this.chefService.getChef(id);

        if (chef != null) {
            // Recupera ed elimina le credenziali associate allo chef
            Credentials credentials = credentialService.findByChefId(id);
            if (credentials != null) {
                this.credentialService.delete(credentials);
            }

            // Elimina lo chef
            this.chefService.deleteById(chef.getId());


        }
        return "redirect:/admin/chef_management";
    }

    @GetMapping("/admin/chef_management/chef_details/{id}")
    public String showChefDetails(@PathVariable Long id, Model model) {
        model.addAttribute("chef", this.chefService.getChef(id));
        return "admin/chef/chef_details";
    }

    @GetMapping("/chef_user/{id}")
    public String getChef(@PathVariable("id") Long id, Model model) {
        Chef chef = this.chefService.getChef(id);
        model.addAttribute("chef", chef);
        model.addAttribute("chefs", this.chefService.getAllChef());
        model.addAttribute("recipes", chef.getRecipes());
        return "chef";
    }



}
