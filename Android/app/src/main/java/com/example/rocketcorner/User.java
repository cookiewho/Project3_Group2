package com.example.rocketcorner;

import java.util.HashMap;

public class User {
    private String username;
    private String email;
    private String password;
    private double balance;
    private HashMap<String, Integer> cart;

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

    public User getAllUsers(){
        return this;
    }

    // iterate through cart
    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", cart=" + cart +
                '}';
    }
}
