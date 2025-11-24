package com.gardencommunity.model;

public class Admin extends User {

    public Admin(int id, String name, String email) {
        super(id, name, email, Role.ADMIN);
    }
}
