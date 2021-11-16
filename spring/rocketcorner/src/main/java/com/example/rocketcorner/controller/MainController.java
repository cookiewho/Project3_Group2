package com.example.rocketcorner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestParam String userID, @RequestParam String password, @RequestParam String itemname) {
        return new ResponseEntity<>("userID purchaseditem", HttpStatus.OK);
    }
}
