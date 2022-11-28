package org.sh.dto;

import java.util.Objects;

public class Superpower {

    private int id;

    private String name;

    public Superpower() {
    }

    public Superpower(String name) {
        this.name = name;
    }

    public Superpower(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Superpower)) return false;
        Superpower that = (Superpower) o;
        return getId() == that.getId() && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
