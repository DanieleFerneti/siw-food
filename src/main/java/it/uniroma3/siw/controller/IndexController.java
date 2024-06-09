package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private ChefService chefService;

    @Autowired
    private RecipeService recipeService;


    @GetMapping("/search")
    public String search(@RequestParam("query") String query) {
        // Controllo se la query corrisponde a un nome di chef
        Chef chef = chefService.findByFullName(query);
        if (chef != null) {
            return "redirect:/chef_user/" + chef.getId();
        }


        Recipe recipe = recipeService.findByName(query);
        if (recipe != null) {
            return "redirect:/recipe/details/" + recipe.getId();
        }

        // Se la query non corrisponde né a un chef né a una ricetta, torna alla pagina di ricerca
        return "redirect:/search"; // Aggiungi un endpoint per la pagina di ricerca
    }

}
