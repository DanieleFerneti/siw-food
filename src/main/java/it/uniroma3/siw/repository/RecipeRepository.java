package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RecipeRepository extends CrudRepository<Recipe,Long> {

    boolean existsByNameAndChef(String name, Chef chef);

    boolean existsByChefAndDescriptionAndIngredientsAndName(Chef chef, String description, Set<Ingredient> ingredients, String name);



    Recipe findByName(String name);

    List<Recipe> findByChefAndDescriptionAndName(Chef chef, String description, String name);
}
