package com.example.rocketcorner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<?> helloWorld() {
        return new ResponseEntity<>("Hello everyone", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> deleteUser(@RequestParam String userId, @RequestParam String password)  {
        return new ResponseEntity<>(userId + " Deleted", HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestParam String userID, @RequestParam String password, @RequestParam String itemname) {
        return new ResponseEntity<>("userID purchaseditem", HttpStatus.OK);
    }
}
