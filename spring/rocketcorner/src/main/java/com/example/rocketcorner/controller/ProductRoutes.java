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

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        try {
            return new ResponseEntity<>(firebaseService.getAllProducts(), HttpStatus.OK);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/products")
    public ResponseEntity<?> items(@RequestParam String ID) {
        try {
            return new ResponseEntity<>(firebaseService.getProduct(ID), HttpStatus.OK);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestParam String adminId, @RequestParam String adminPassword, @RequestParam String name, @RequestParam String description, @RequestParam String imageURL, @RequestParam double price) {
        if (!UserRoutes.isAdmin(adminId, adminPassword)) {
            return new ResponseEntity<>("Not Admin", HttpStatus.FORBIDDEN);
        }
        try {
            String ID = firebaseService.saveProductDetails(new Product(name, description, imageURL, price));
            return new ResponseEntity<>(ID, HttpStatus.OK);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //look into maybe using objectmapper for this one
    @PatchMapping("/products")
    public ResponseEntity<?> updateProduct(@RequestParam String adminId, @RequestParam String adminPassword, @RequestParam String ID, @RequestParam Optional<String> name, @RequestParam Optional<String> description, Optional<String> imageURL, Optional<Double> price) {
        if (!UserRoutes.isAdmin(adminId, adminPassword)) {
            return new ResponseEntity<>("Not Admin", HttpStatus.FORBIDDEN);
        }
        try {
            HashMap<String, Product> currMap = firebaseService.getProduct(ID);
            Product currProduct = currMap.get(ID);

            if(name.isPresent()) {
                currProduct.setName(name.get());
            }

            if(description.isPresent()) {
                currProduct.setDesc(description.get());
            }

            if(imageURL.isPresent()) {
                currProduct.setImgLink(imageURL.get());
            }

            if(price.isPresent()) {
                currProduct.setPrice(price.get());
            }

            String myID = firebaseService.updateProductDetails(ID, currProduct);
            return new ResponseEntity<>(ID + " Updated", HttpStatus.OK);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}