package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class IngredientValidator implements Validator {
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 20;
    private static final int QUANTITY_MAX_LENGTH = 7;

    @Autowired
    private IngredientService ingredientService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Ingredient.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ingredient ingredient = (Ingredient) target;

        // Validate name length
        String name = ingredient.getName();
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            errors.rejectValue("name", "ingredient_name.size");
        }

        // Validate quantity length
        String quantity = ingredient.getQuantity();
        if (quantity.length() > QUANTITY_MAX_LENGTH) {
            errors.rejectValue("quantity", "quantity.size");
        }


        if (this.ingredientService.alreadyExists(ingredient)) {
            errors.reject("ingredient.duplicato");
        }
    }
}

