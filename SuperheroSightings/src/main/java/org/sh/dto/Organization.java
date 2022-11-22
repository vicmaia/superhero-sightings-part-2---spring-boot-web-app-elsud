package org.sh.dto;

import java.util.List;

public class Organization {

    private int id;
    private String name;
    private String description;
    private String address;
    private List<Superhero> members;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Superhero> getMembers() {
        return members;
    }

    public void setMembers(List<Superhero> members) {
        this.members = members;
    }
}
