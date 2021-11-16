package com.example.rocketcorner.controller;

import com.example.rocketcorner.objects.Product;
import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
public class ProductRoutes {
    @Autowired
    FirebaseService firebaseService;

    @GetMapping("/products")
    public ResponseEntity<?> items(@RequestParam Optional<String> name) {
        if(name.isPresent()) {
            return new ResponseEntity<>(new Product("Slowpoke Tail", "Slowpoke Tail for Slowpoke Soup", "https://i.etsystatic.com/7926094/r/il/1cbb75/872549159/il_570xN.872549159_swi4.jpg", 9.99), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Arrays.asList(new Product("Premium Slowpoke Tail", "Slowpoke Tail for Slowpoke Soup", "https://i.etsystatic.com/7926094/r/il/1cbb75/872549159/il_570xN.872549159_swi4.jpg", 9.99), new Product("Slowpoke Tail", "Slowpoke Tail For Consumption", "https://i.etsystatic.com/7926094/r/il/1cbb75/872549159/il_570xN.872549159_swi4.jpg", 19.99)), HttpStatus.OK);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestParam String name, @RequestParam String description, @RequestParam String imageURL, @RequestParam double price) {
        return new ResponseEntity<>("Product id # goes here", HttpStatus.OK);
    }

    @PatchMapping("/products")
    public ResponseEntity<?> updateProduct(@RequestParam String productId, @RequestParam)

}