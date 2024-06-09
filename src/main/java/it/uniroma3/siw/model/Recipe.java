package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.*;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @NotNull
    private String name;


    @ElementCollection
    @Column(name = "image")
    private Set<String> imagePaths;

    @NotNull
    @ManyToMany
    private Set<Ingredient> ingredients;


    @NotBlank
    @NotNull
    private String description;



    @NotNull
    @ManyToOne
    private Chef chef;


    public Recipe() {
        this.ingredients = new HashSet<Ingredient>();
    }

    public Recipe(String name, String description, Set<String> imagePaths, Chef chef, Set<Ingredient> ingredients) {
        this.name = name;
        this.description = description;
        this.imagePaths = imagePaths;
        this.chef = chef;
        this.ingredients = ingredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(Set<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) && Objects.equals(name, recipe.name) && Objects.equals(description, recipe.description) && Objects.equals(chef, recipe.chef) && Objects.equals(ingredients, recipe.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, chef, ingredients);
    }

    public String getDirectoryName() {
        return "recipe" + this.getId();
    }
}
