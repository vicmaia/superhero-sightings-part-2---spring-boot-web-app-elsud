package org.sh.dto;

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
}
