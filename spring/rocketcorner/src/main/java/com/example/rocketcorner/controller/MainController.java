package com.example.rocketcorner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class MainController {

    @GetMapping("/")
    public ResponseEntity<?> helloWorld() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

}
