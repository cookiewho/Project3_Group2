package com.example.rocketcorner.objects;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class User {
    private String username;
    private String email;
    private String password;
    private double balance;
    private HashMap<String, Integer> cart;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = 50.10;
        this.cart = new HashMap<String, Integer>();
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

    public Map<String, Integer> getCart() {
        return cart;
    }

    public void addToCart(String itemId, Integer quantity) {
        if(this.cart.containsKey(itemId)) {
            quantity = this.cart.get(itemId) + quantity;
        }
        this.cart.put(itemId, quantity);
    }
}
