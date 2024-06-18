package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.RecipeValidator;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.presentation.FileStorer;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeValidator recipeValidator;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private ChefService chefService;

    @GetMapping("/admin/recipe_management")
    public String getAllRecipesAdmin(Model model) {
        model.addAttribute("recipes", this.recipeService.getAllRecipes());
        return "admin/recipe/recipe_management";
    }

    @GetMapping("/chef/{chef_id}/recipe_management")
    public String getAllRecipesChef(Model model, @PathVariable("chef_id") Long chef_id) {
        Chef chef = this.chefService.getChef(chef_id);
        model.addAttribute("chef", chef);
        model.addAttribute("recipes", chef.getRecipes());
        return "chef/recipe/recipe_management";
    }

    @GetMapping("/admin/recipe_management/add_recipe")
    public String showAddRecipeFormAdmin(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
        model.addAttribute("chefs",this.chefService.getAllChef());
        return "admin/recipe/create_recipe";
    }

    @GetMapping("/chef/{chef_id}/recipe_management/add_recipe")
    public String showAddRecipeFormChef(Model model, @PathVariable("chef_id") Long chef_id) {
        Chef chef = this.chefService.getChef(chef_id);
        model.addAttribute("chef_id", chef_id);
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("recipes", chef.getRecipes());
        model.addAttribute("ingredients", this.ingredientService.getAllIngredients());

        return "chef/recipe/create_recipe";
    }

    @PostMapping("/admin/recipe_management/add_recipe")
    public String addRecipeAdmin(@Valid @ModelAttribute("recipe") Recipe recipe,
                                 BindingResult bindingResult,
                                 Model model,
                                 @RequestParam("files") MultipartFile[] files, Errors errors) {

        this.recipeValidator.validate(recipe, bindingResult);

        if(this.recipeService.alreadyExists(recipe)) {
            errors.reject("recipe.duplicato");
            model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
            model.addAttribute("chefs",this.chefService.getAllChef());
            return "admin/recipe/create_recipe";

        }else if (!bindingResult.hasErrors()) {
            this.recipeService.save(recipe);
            String defaultPath = "/images/"+ recipe.getDirectoryName() + "/";

            // Gestione dei file caricati
            Set<String> imagePaths = new HashSet<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String filePath = defaultPath + FileStorer.store(file, recipe.getDirectoryName());
                    imagePaths.add(filePath);
                }
                else {
                    String filePath = "/images/" + FileStorer.store(file, "images/default-recipe.png");
                    imagePaths.add(filePath);
                }
            }
            recipe.setImagePaths(imagePaths);

            // Salva la ricetta
            this.recipeService.save(recipe);
            return "redirect:/admin/recipe_management";
        } else {
            model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
            model.addAttribute("chefs",this.chefService.getAllChef());
            return "admin/recipe/create_recipe";
        }
    }


    @PostMapping("/chef/{chef_id}/recipe_management/add_recipe")
    public String addRecipeChef(@Valid @ModelAttribute("recipe") Recipe recipe,
                                BindingResult bindingResult,
                                Model model,
                                @PathVariable("chef_id") Long chef_id,
                                @RequestParam("files") MultipartFile[] files) {
        Chef chef = this.chefService.getChef(chef_id);
        recipe.setChef(chef);
        model.addAttribute("chef_id", chef_id);
        model.addAttribute("recipes", chef.getRecipes());
        model.addAttribute("chefs",this.chefService.getAllChef());

        this.recipeValidator.validate(recipe, bindingResult);

        if (!bindingResult.hasErrors() && !this.recipeService.alreadyExists(recipe)) {
            this.recipeService.save(recipe);
            String defaultPath = "/images/"+ recipe.getDirectoryName() + "/";
            // Gestione dei file caricati
            Set<String> imagePaths = new HashSet<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String filePath = defaultPath + FileStorer.store(file, recipe.getDirectoryName());
                    imagePaths.add(filePath);
                }
                else {
                    String filePath = "/images/" + FileStorer.store(file, "images/default-recipe.png");
                    imagePaths.add(filePath);
                }
            }
            recipe.setImagePaths(imagePaths);

            // Salva la ricetta
            this.recipeService.save(recipe);
            return "redirect:/chef/{chef_id}/recipe_management";
        } else {
            model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
            model.addAttribute("chefs",this.chefService.getAllChef());
            return "chef/recipe/create_recipe";
        }
    }


    @GetMapping("/admin/recipe_management/edit_recipe/{id}")
    public String showEditRecipeFormAdmin(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.getRecipe(id));
        model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
        return "admin/recipe/edit_recipe";
    }

    @GetMapping("/chef/{chef_id}/recipe_management/edit_recipe/{id}")
    public String showEditRecipeFormChef(@PathVariable Long id, Model model, @PathVariable("chef_id") Long chef_id) {
        Chef chef = this.chefService.getChef(chef_id);
        model.addAttribute("recipe", recipeService.getRecipe(id));
        model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
        model.addAttribute("recipes", chef.getRecipes());
        model.addAttribute("chef", chef);
        return "chef/recipe/edit_recipe";
    }

    @PostMapping("/admin/recipe_management/{id}")
    public String editRecipe(@PathVariable Long id, @Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult, @RequestParam("file") MultipartFile[] files,Model model) {

        this.recipeValidator.validate(recipe,bindingResult);

        if (!bindingResult.hasErrors()){
            this.recipeService.save(recipe);
            String defaultPath = "/images/"+ recipe.getDirectoryName() + "/";
            // Process and save new image files
            Set<String> imagePaths = new HashSet<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String filePath = defaultPath + FileStorer.store(file, recipe.getDirectoryName());
                    imagePaths.add(filePath);
                }
                else {
                    String filePath = "/images/" + FileStorer.store(file, "images/default-recipe.png");
                    imagePaths.add(filePath);
                }
            }
            recipe.setImagePaths(imagePaths);


            this.recipeService.save(recipe);
            return "redirect:/admin/recipe_management";
        } else {

            model.addAttribute("ingredients", this.ingredientService.getAllIngredients());
            model.addAttribute("chef",this.chefService.getAllChef());

            return "admin/recipe/edit_recipe";
        }
    }


    @PostMapping("/chef/{chef_id}/recipe_management/{id}")
    public String editRecipeChef(@PathVariable Long id, @Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult, @RequestParam("file") MultipartFile[] files, Model model, @PathVariable("chef_id") Long chef_id) {

        this.recipeValidator.validate(recipe,bindingResult);

        if ( !bindingResult.hasErrors()) {
            // Salva la ricetta
            this.recipeService.save(recipe);
            String defaultPath = "/images/"+ recipe.getDirectoryName() + "/";


            // Processa e salva i nuovi file di immagine
            Set<String> imagePaths = new HashSet<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String filePath = defaultPath + FileStorer.store(file, recipe.getDirectoryName());
                    imagePaths.add(filePath);
                }
                else {
                    String filePath = "/images/" + FileStorer.store(file, "images/default-recipe.png");
                    imagePaths.add(filePath);
                }

            }
            recipe.setImagePaths(imagePaths);

            // Salva di nuovo la ricetta con i percorsi delle immagini aggiornati
            this.recipeService.save(recipe);

            return "redirect:/chef/{chef_id}/recipe_management";
        } else {

            model.addAttribute("ingredients", this.ingredientService.getAllIngredients());

            return "chef/recipe/edit_recipe"; // Supponendo che esista un template "edit_recipe" per chef
        }
    }




    @GetMapping("/admin/recipe_management/delete_recipe/{id}")
    public String deleteRecipeAdmin(@PathVariable Long id, Model model) {
        Recipe recipe = this.recipeService.getRecipe(id);
        boolean presenteInChef = checkIfRecipePresentInAnyChef(recipe);

        model.addAttribute("r", recipe);
        if (!presenteInChef) {
            for(String image : recipe.getImagePaths()){
                if(image.equals("/images/default-recipe.png")){
                    this.recipeService.deleteById(id);
                }
                else {
                    this.recipeService.deleteById(id);
                    FileStorer.dirEmptyEndDelete(recipe.getDirectoryName());
                }
            }
            return "redirect:/admin/recipe_management";
        } else {
            model.addAttribute("recipes", this.recipeService.getAllRecipes());
            return "admin/recipe/recipe_management";
        }
    }

    @GetMapping("/chef/{chef_id}/recipe_management/delete_recipe/{id}")
    public String deleteRecipeChef(@PathVariable Long id, Model model, @PathVariable("chef_id") Long chef_id) {
        Chef chef = this.chefService.getChef(chef_id);
        Recipe recipe = this.recipeService.getRecipe(id);
        boolean presenteInChef = chef.getRecipes().contains(recipe);

        model.addAttribute("chef_id", chef_id);
        model.addAttribute("recipes", chef.getRecipes());
        model.addAttribute("r", recipe);
        if (presenteInChef) {
            for(String image : recipe.getImagePaths()) {
                if (image.equals("/images/default-recipe.png")) {
                    this.recipeService.deleteById(id);
                } else {
                    this.recipeService.deleteById(id);
                    FileStorer.dirEmptyEndDelete(recipe.getDirectoryName());
                }
            }
            return "redirect:/chef/{chef_id}/recipe_management";
        } else {
            return "chef/recipe/recipe_management";
        }
    }

    @GetMapping("/admin/recipe_management/recipe_details/{id}")
    public String showRecipeDetailsAdmin(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", this.recipeService.getRecipe(id));
        return "admin/recipe/recipe_details";
    }

    @GetMapping("/chef/{chef_id}/recipe_management/recipe_details/{id}")
    public String showRecipeDetailsChef(@PathVariable Long id, Model model, @PathVariable("chef_id") Long chef_id) {
        Chef chef = this.chefService.getChef(chef_id);
        model.addAttribute("recipe", this.recipeService.getRecipe(id));
        model.addAttribute("chef", chef);
        model.addAttribute("recipes", chef.getRecipes());
        return "chef/recipe/recipe_details";
    }

    @GetMapping("/recipe/details/{id}")
    public String getRecipe(@PathVariable("id") Long id, Model model) {
        Recipe recipe = this.recipeService.getRecipe(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("chef", recipe.getChef());
        model.addAttribute("chefs",this.chefService.getAllChef());
        return "recipe";
    }

    @GetMapping("/recipe")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", this.recipeService.getAllRecipes());
        return "recipe";
    }


    @GetMapping("/admin/recipe_management/view_images/{recipeId}")
    public String viewRecipeImagesAdmin(@PathVariable("recipeId") Long recipeId, Model model) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        if (recipe != null) {
            model.addAttribute("recipe", recipe);
            return "admin/recipe/view_images"; // Nome della pagina Thymeleaf che visualizza le immagini
        } else {
            return "redirect:/error"; // Reindirizza alla pagina di errore se la ricetta non esiste
        }
    }

    @GetMapping("/chef/{chef_id}/recipe_management/view_images/{recipeId}")
    public String viewRecipeImagesChef(@PathVariable("recipeId") Long recipeId, Model model) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        if (recipe != null) {
            model.addAttribute("recipe", recipe);
            model.addAttribute("chef_id",recipe.getChef().getId());
            return "chef/recipe/view_images"; // Nome della pagina Thymeleaf che visualizza le immagini
        } else {
            return "redirect:/error"; // Reindirizza alla pagina di errore se la ricetta non esiste
        }
    }
    private boolean checkIfRecipePresentInAnyChef(Recipe recipe) {
        for (Chef chef : this.chefService.getAllChef()) {
            if (chef.getRecipes().contains(recipe)) {
                return true;
            }
        }
        return false;
    }

}
