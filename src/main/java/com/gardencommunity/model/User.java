package com.gardencommunity.model;

public abstract class User {

    public enum Role {
        ADMIN,
        GARDENER
    }

    protected int id;
    protected String name;
    protected String email;
    protected Role role;

    public User(int id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " <" + email + "> (" + role + ")";
    }
}
