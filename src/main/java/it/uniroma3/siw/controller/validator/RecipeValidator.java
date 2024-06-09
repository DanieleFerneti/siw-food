package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class RecipeValidator implements Validator {

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 50;
    private static final int DESCRIPTION_MIN_WORDS = 2;
    private static final int DESCRIPTION_MAX_WORDS = 200;

    @Autowired
    private RecipeService recipeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Recipe.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Recipe recipe = (Recipe) target;


        // Validate name length
        String name = recipe.getName();
        // Validate description word count
        String description = recipe.getDescription();


        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            errors.rejectValue("name", "name.size");
        }



        if (description.length() < DESCRIPTION_MIN_WORDS || description.length() > DESCRIPTION_MAX_WORDS) {
            errors.rejectValue("description", "description.size");
        }

        // Validate ingredients
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            errors.rejectValue("ingredients",  "required");
        }

        // Validate chef
        if (recipe.getChef() == null) {
            errors.rejectValue("chef", "required");
        }

        /*
        // Validate if chef already exists
        if (this.recipeService.alreadyExists(recipe)) {
            errors.reject("recipe.duplicato");
        }
        */


        if(this.recipeService.youCantEdit(recipe)){
            errors.reject("recipe.modificato");
        }
    }
}
