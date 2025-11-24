package com.gardencommunity.model;

public class Gardener extends User {

    private String bio = "";

    public Gardener(int id, String name, String email) {
        super(id, name, email, Role.GARDENER);
    }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }
}
