package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private boolean fixed;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Expense> expenses;

    // Constructores, Getters y setters
    public Category() {}

    public Category(String name, String description, boolean fixed) {
        this.name = name;
        this.description = description;
        this.fixed = fixed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isFixed() { return fixed; }
    public void setFixed(boolean fixed) { this.fixed = fixed; }
    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }
} 