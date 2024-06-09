package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IngredientService {

        @Autowired
        private IngredientRepository ingredientRepository;

        @Transactional
        public void save(Ingredient ingredient) {
            this.ingredientRepository.save(ingredient);
        }

        @Transactional
        public Ingredient getIngredient(Long id) {
            return this.ingredientRepository.findById(id).get();
        }

        @Transactional
        public Set<Ingredient> getAllIngredients() {
            Set<Ingredient> ingredients = new HashSet<Ingredient>();
            for(Ingredient i : this.ingredientRepository.findAll()) {
                ingredients.add(i);
            }
            return ingredients;
        }

        @Transactional
        public boolean alreadyExists(Ingredient ingredient) {
            return this.ingredientRepository.existsByNameAndQuantity(ingredient.getName(), ingredient.getQuantity());
        }

        @Transactional
        public void deleteById(Long id) {
            this.ingredientRepository.deleteById(id);
        }



}
