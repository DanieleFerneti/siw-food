package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional
    public void save(Recipe recipe) {
        this.recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe getRecipe(Long id) {
        return this.recipeRepository.findById(id).get();
    }

    @Transactional
    public Set<Recipe> getAllRecipes() {
        Set<Recipe> recipes = new HashSet<Recipe>();
        for(Recipe p : this.recipeRepository.findAll()) {
            recipes.add(p);
        }
        return recipes;
    }

    @Transactional
    public Recipe findByName(String name) {
        // Cerca la ricetta nel repository per nome
        return recipeRepository.findByName(name);
    }

    @Transactional
    public boolean alreadyExists(Recipe recipe) {
       List<Recipe> recipes =  this.recipeRepository.findByChefAndDescriptionAndNameAndImagePaths(recipe.getChef(),recipe.getDescription(),recipe.getName(),recipe.getImagePaths());
        for (Recipe r : recipes) {
            if (r.getIngredients().containsAll(recipe.getIngredients()) && recipe.getIngredients().containsAll(r.getIngredients()) ){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean youCantEdit(Recipe recipe) {
        List<Recipe> recipes = this.recipeRepository.findByChefAndDescriptionAndName(recipe.getChef(), recipe.getDescription(), recipe.getName());
        for (Recipe r : recipes) {
            if (r.getIngredients().containsAll(recipe.getIngredients()) && recipe.getIngredients().containsAll(r.getIngredients()) &&
                    r.getImagePaths().containsAll(recipe.getImagePaths()) && recipe.getImagePaths().containsAll(r.getImagePaths())) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteById(Long id) {
        this.recipeRepository.deleteById(id);
    }

}
