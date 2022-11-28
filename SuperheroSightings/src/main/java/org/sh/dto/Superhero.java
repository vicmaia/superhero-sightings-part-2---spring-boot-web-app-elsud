package org.sh.dto;

import java.util.Objects;

public class Superhero {

    private int id;
    private String name;
    private String description;
    private Superpower superpower;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Superhero)) return false;
        Superhero superhero = (Superhero) o;
        return getId() == superhero.getId() && getName().equals(superhero.getName()) && Objects.equals(getDescription(), superhero.getDescription()) && getSuperpower().equals(superhero.getSuperpower());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getSuperpower());
    }

    public Superhero() {
    }

    public Superhero(
            int id, String name, String description, int superpowerId, String superpowerName
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.superpower = new Superpower(superpowerId, superpowerName);
    }

    public Superhero(
            String name, String description, int superpowerId, String superpowerName
    ) {
        this.name = name;
        this.description = description;
        this.superpower = new Superpower(superpowerId, superpowerName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Superpower getSuperpower() {
        return superpower;
    }

    public void setSuperpower(Superpower superpower) {
        this.superpower = superpower;
    }

}
