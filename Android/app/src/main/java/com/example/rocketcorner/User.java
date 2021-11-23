package com.example.rocketcorner;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String username;
    public String email;
    private String password;
    private double balance;
    private HashMap<String, Integer> cart;

    public User() {

    }

    public User(String fullName, String email) {
        this.username = fullName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public HashMap<String, Integer> getCart() {
        return cart;
    }

    public void setCart(HashMap<String, Integer> cart) {
        this.cart = cart;
    }


    // iterate through cart
    @Override
    public String toString() {
        String s = "User{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", cart = ";

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            s += "\t Key:" + entry.getKey() + " Value:" + entry.getValue() + "\n";
        }

        return s;
    }
}
