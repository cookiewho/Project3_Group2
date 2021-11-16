package com.example.rocketcorner.controller;

import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UserRoutes {
    @Autowired
    FirebaseService firebaseService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() throws ExecutionException, InterruptedException {
        HashMap<String, User> usersHashMap;

        try {
            usersHashMap = firebaseService.getAllUsers();
            return new ResponseEntity<>(usersHashMap, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestParam String userId, @RequestParam String new_username, @RequestParam String new_email, @RequestParam String new_password) throws ExecutionException, InterruptedException {
        HashMap<String, User> userHashMap;
        HashMap<String, Object> updates = new HashMap<>();

        updates.put("username", new_username);
        updates.put("email", new_email);
        updates.put("password", new_password);

        try {
            String userId_updated = firebaseService.updateUserDetails(userId, updates);
            userHashMap = firebaseService.getUser(userId_updated);
            return new ResponseEntity<>(userHashMap, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getSpecificUser(@RequestParam String userId) throws ExecutionException, InterruptedException {
        HashMap<String, User> userHashMap;

        try {
            userHashMap = firebaseService.getUser(userId);
            return new ResponseEntity<>(userHashMap, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/newUser")
    public ResponseEntity<?> newUser(@RequestParam String username, @RequestParam String password) {
        return new ResponseEntity<>("Unique User Id # Here", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String userId, @RequestParam String password)  {
        return new ResponseEntity<>(userId + " Deleted", HttpStatus.OK);
    }

}