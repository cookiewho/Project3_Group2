package com.example.rocketcorner.controller;

import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class userRoutes {

    @Autowired
    FirebaseService firebaseService;


    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() throws ExecutionException, InterruptedException {
        HashMap<String, User> userHashMap;

        try {
            userHashMap = firebaseService.getAllUsers();
            return new ResponseEntity<>(userHashMap, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}