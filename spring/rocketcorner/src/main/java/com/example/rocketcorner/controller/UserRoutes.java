package com.example.rocketcorner.controller;

import com.example.rocketcorner.objects.Admin;
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
            if(userId_updated != null) {
                return new ResponseEntity<>(userId_updated, HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid ID Provided", HttpStatus.FORBIDDEN);
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
            if(userHashMap != null) {
                return new ResponseEntity<>(userHashMap, HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid ID Provided", HttpStatus.FORBIDDEN);
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
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws ExecutionException, InterruptedException {
        try {
            HashMap<String, User> allUsersHash = firebaseService.getAllUsers();
            for (Map.Entry<String, User> entry : allUsersHash.entrySet()) {
                if (entry.getValue().getUsername().equals(username)) {
                    if (entry.getValue().getPassword().equals(password)) {

                        return new ResponseEntity<>(entry.getKey(), HttpStatus.OK);
                    }
                }
            }
            return new ResponseEntity<>("Invalid Login Credentials", HttpStatus.FORBIDDEN);
        } catch (Exception e){
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String userId, @RequestParam String password, @RequestParam(required = false) String adminId) throws ExecutionException, InterruptedException {
        try {
            boolean verified = false;
            if (adminId != null && adminId.equals(Admin.ADMIN_ID)){
                if(password.equals(Admin.ADMIN_PASSWORD)){
                    verified = true;
                }
            } else {
                HashMap<String, User> userHash = firebaseService.getUser(userId);
                if (userHash != null) {
                    for (Map.Entry<String, User> entry : userHash.entrySet()) {
                        if (entry.getValue().getPassword().equals(password)) {
                            verified = true;
                        }
                    }
                } else{
                    return new ResponseEntity<>("INVALID USER ID", HttpStatus.FORBIDDEN);
                }
            }

            if (verified){
                boolean userDeleted = firebaseService.deleteUser(userId);
                if (userDeleted) {
                    return new ResponseEntity<>("User " + userId + " Deleted", HttpStatus.OK);
                }
                return new ResponseEntity<>("UNABLE TO DELETE USER", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>("Invalid Authorization", HttpStatus.UNAUTHORIZED);

        } catch (Exception e){
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}