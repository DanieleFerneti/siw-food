package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,Long> {

    public boolean existsByNameAndQuantity(String name, String quantity);
}
