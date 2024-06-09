package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


@Entity
public class Chef {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String surname;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate data;

    private String imageFileName;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.REMOVE)
    private Set<Recipe> recipes;


    public Chef() {
        this.recipes = new HashSet<Recipe>();
    }

    public Chef(String name, String surname, LocalDate data, String imageFileName, Set<Recipe> recipes) {
        this.name = name;
        this.surname = surname;
        this.data = data;
        this.imageFileName = imageFileName;
        this.recipes = recipes;

    }


    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getImageFileName() {
        return imageFileName;
    }


    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chef chef = (Chef) o;
        return Objects.equals(id, chef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getDirectoryName() {
        return "user" + this.getId();
    }
}
