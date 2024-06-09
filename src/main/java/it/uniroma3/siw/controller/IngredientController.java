package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.IngredientValidator;
import it.uniroma3.siw.controller.validator.RecipeValidator;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Controller
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;


    @Autowired
    private IngredientValidator ingredientValidator;

    @Autowired
    private ChefService chefService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeValidator recipeValidator;


    @GetMapping("/admin/ingredient_management")
    public String getAllIngredientsAdmin(Model model) {
        Set<Ingredient> ingredients = this.ingredientService.getAllIngredients();
        model.addAttribute("ingredients", ingredients);

        return "admin/ingredient/ingredient_management";
    }

    @GetMapping("/chef/{chef_id}/ingredient_management")
    public String getAllIngredientsChef(Model model, @PathVariable("chef_id") Long chef_id) {
        Set<Ingredient> ingredients = this.ingredientService.getAllIngredients();

        model.addAttribute("ingredients", ingredients);

        return "chef/ingredient/ingredient_management";
    }

    @GetMapping("/admin/ingredient_management/create_ingredient")
    public String getAddIngredientFormAdmin(Model model) {
        model.addAttribute("ingredient", new Ingredient());

        return "admin/ingredient/create_ingredient";
    }

    @GetMapping("/chef/{chef_id}/ingredient_management/create_ingredient")
    public String getAddIngredientFormChef(Model model, @PathVariable("chef_id") Long chef_id) {
        model.addAttribute("ingredient", new Ingredient());

        return "chef/ingredient/create_ingredient";
    }

    @PostMapping("/admin/ingredient_management/add_ingredient")
    public String addIngredientAdmin(@Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult ingredientBindingResult,BindingResult recipeBindingResult,Model model) {

        this.ingredientValidator.validate(ingredient, ingredientBindingResult);

        if (!ingredientBindingResult.hasErrors()) {
            this.ingredientService.save(ingredient);
            return "redirect:/admin/ingredient_management";
        } else {
            return "admin/ingredient/create_ingredient";
        }
    }

    @PostMapping("/chef/{chef_id}/ingredient_management/add_ingredient")
    public String addIngredientChef(@Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult, @PathVariable("chef_id") Long chef_id) {
        this.ingredientValidator.validate(ingredient, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.ingredientService.save(ingredient);
            return "redirect:/chef/{chef_id}/ingredient_management";
        } else {
            return "chef/ingredient/create_ingredient";
        }
    }

    @GetMapping("/admin/ingredient_management/edit_ingredient/{id}")
    public String getEditIngredientFormAdmin(@PathVariable Long id, Model model) {
        model.addAttribute("ingredient", ingredientService.getIngredient(id));
        return "admin/ingredient/edit_ingredient";
    }

    @GetMapping("/chef/{chef_id}/ingredient_management/edit_ingredient/{id}")
    public String getEditIngredientFormChef(@PathVariable Long id, Model model, @PathVariable("chef_id") Long chef_id) {
        model.addAttribute("ingredient", ingredientService.getIngredient(id));
        return "chef/ingredient/edit_ingredient";
    }

    @PostMapping("/admin/ingredient_management/{id}")
    public String editIngredientAdmin(@PathVariable Long id, @Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult,Model model) {
        this.ingredientValidator.validate(ingredient, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.ingredientService.save(ingredient);
            return "redirect:/admin/ingredient_management";
        } else {
            return "admin/ingredient/edit_ingredient";
        }
    }

    @PostMapping("/chef/{chef_id}/ingredient_management/{id}")
    public String editIngredientChef(@PathVariable Long id, @Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult, @PathVariable("chef_id") Long chef_id) {
        this.ingredientValidator.validate(ingredient, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.ingredientService.save(ingredient);
            return "redirect:/chef/{chef_id}/ingredient_management";
        } else {
            return "chef/ingredient/edit_ingredient";
        }
    }

    @GetMapping("/admin/ingredient_management/delete_ingredient/{id}")
    public String deleteIngredientAdmin(@PathVariable Long id, Model model) {
        boolean presenteInRecipe = false;
        Ingredient ingredient = this.ingredientService.getIngredient(id);
        for (Recipe p : this.recipeService.getAllRecipes()) {
            if (p.getIngredients().contains(ingredient)) {
                presenteInRecipe = true;
                break;
            }
        }
        model.addAttribute("ingr", ingredient);
        if (!presenteInRecipe) {
            this.ingredientService.deleteById(id);
            return "redirect:/admin/ingredient_management";
        } else {
            Set<Ingredient> ingredients = this.ingredientService.getAllIngredients();
            model.addAttribute("ingredients", ingredients);
            return "admin/ingredient/ingredient_management";
        }
    }

    @GetMapping("/chef/{chef_id}/ingredient_management/delete_ingredient/{id}")
    public String deleteIngredientChef(@PathVariable Long id, Model model, @PathVariable("chef_id") Long chef_id) {
        boolean presenteInRecipe = false;
        Ingredient ingredient = this.ingredientService.getIngredient(id);
        for (Recipe p : this.recipeService.getAllRecipes()) {
            if (p.getIngredients().contains(ingredient)) {
                presenteInRecipe = true;
                break;
            }
        }
        model.addAttribute("ingr", ingredient);
        if (!presenteInRecipe) {
            this.ingredientService.deleteById(id);
            return "redirect:/chef/{chef_id}/ingredient_management";
        } else {
            Set<Ingredient> ingredients = this.ingredientService.getAllIngredients();
            model.addAttribute("ingredients", ingredients);
            return "chef/ingredient/ingredient_management";
        }
    }

    @GetMapping("/ingredient/{id}")
    public String getIngredient(@PathVariable("id") Long id, Model model) {
        model.addAttribute("ingredient", this.ingredientService.getIngredient(id));
        model.addAttribute("chefs", this.chefService.getAllChef());
        model.addAttribute("recipes", this.recipeService.getAllRecipes());
        return "ingredient";
    }

    @GetMapping("/ingredient")
    public String getIngredients(Model model) {
        Set<Ingredient> ingredients = this.ingredientService.getAllIngredients();
        model.addAttribute("ingredients", ingredients);
        return "ingredient";
    }
}
