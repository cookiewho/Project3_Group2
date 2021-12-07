package com.example.rocketcorner.controller;

import com.example.rocketcorner.objects.Admin;
import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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

                // VALIDATE USERNAME AND EMAIL NOT DUPLICATED
                String validated = duplicateCreds(new_username, new_email, userId_updated);
                if(validated.equals("INTERNAL SERVER ERROR")){
                    return new ResponseEntity<>(validated, HttpStatus.INTERNAL_SERVER_ERROR);
                } else if(!validated.equals("valid")){
                    return new ResponseEntity<>(validated, HttpStatus.FORBIDDEN);
                }

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
    public ResponseEntity<?> newUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) throws ExecutionException, InterruptedException {
        try{
            // VALIDATE USERNAME AND EMAIL NOT DUPLICATED
            String validated = duplicateCreds(username, email, null);
            if(validated.equals("INTERNAL SERVER ERROR")){
                return new ResponseEntity<>(validated, HttpStatus.INTERNAL_SERVER_ERROR);
            } else if(!validated.equals("valid")){
                return new ResponseEntity<>(validated, HttpStatus.FORBIDDEN);
            }

            // ONCE CREDENTAILS VALIDATED CREATE USER
            User newUser = new User(username, email, password);
            String userId = firebaseService.saveUserDetails(newUser);
            return new ResponseEntity<>(userId, HttpStatus.OK);

        } catch (Exception e){
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            boolean verified = isAdmin(adminId, password);
            if (!verified){
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

            if (verified && !userId.equals(Admin.ADMIN_ID)){
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

    @PatchMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestParam String userId, @RequestParam String cartUpdatesMapStr) throws ExecutionException, InterruptedException, JsonProcessingException {
        try {
            cartUpdatesMapStr = "{"+ cartUpdatesMapStr + "}";
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Integer> cartUpdatesMap;

            // Make sure cart format is correct
            try {
                cartUpdatesMap = mapper.readValue(cartUpdatesMapStr, Map.class);
            } catch (JsonParseException e){
                return new ResponseEntity<>("Invalid Cart Formatting", HttpStatus.FORBIDDEN);
            }

            // call db to update cart
            Map<String, Integer> updatedCart = firebaseService.updateCart(userId, cartUpdatesMap);
            if(updatedCart != null) {
                return new ResponseEntity<>(updatedCart, HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid ID Provided", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getCart")
    public ResponseEntity<?> getCart(@RequestParam String userId, @RequestParam String password) {
        try {
            User currUser = firebaseService.getUser(userId).get(userId);

            if(!currUser.getPassword().equals(password)) {
                return new ResponseEntity<>("Invalid PW", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(currUser.getCart(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/clearCart")
    public ResponseEntity<?> clearCart(@RequestParam String userId, @RequestParam String password) {
        try {
            HashMap<String, User> allUsersHash = firebaseService.getAllUsers();
            User currUser = firebaseService.getUser(userId).get(userId);

            if(!currUser.getPassword().equals(password)) {
                return new ResponseEntity<>("Invalid PW", HttpStatus.FORBIDDEN);
            }

            double total_spent = 0;

            Map<String, Integer> myMap = currUser.getCart();

            for(Map.Entry<String, Integer> x : myMap.entrySet()) {
                double price = firebaseService.getProduct(x.getKey()).get(x.getKey()).getPrice();
                total_spent +=  price * x.getValue();
            }

            if(total_spent > currUser.getBalance()) {
                return new ResponseEntity<>("Not Enough Money", HttpStatus.BAD_REQUEST);
            } else {
                currUser.setBalance(currUser.getBalance() - total_spent);
                firebaseService.updateCart(userId, new HashMap<>());

                return new ResponseEntity<>("Purchased $" + String.valueOf(total_spent) + " of items", HttpStatus.OK);
            }

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String duplicateCreds(String username, String email, String id) throws ExecutionException, InterruptedException {
        try {
            if(id == null){
                id = "";
            }
            String inValidMsg = "";
            HashMap<String, User> allUsersHash = firebaseService.getAllUsers();
            for (Map.Entry<String, User> entry : allUsersHash.entrySet()) {
                if(!(id.equals(entry.getKey()))) {
                    if (entry.getValue().getUsername().equals(username)) {
                        if (inValidMsg.length() == 0) {
                            inValidMsg += "ERROR: ";
                        }

                        inValidMsg += "Username is already taken. ";
                    }

                    if (entry.getValue().getEmail().equals(email)) {
                        if (inValidMsg.length() == 0) {
                            inValidMsg += "ERROR: ";
                        }

                        inValidMsg += "Email is already taken. ";
                    }
                }
            }
            if(inValidMsg.length() != 0){
                return inValidMsg;
            }
            return "valid";
        } catch (Exception e){
            System.out.print(e);
            return "INTERNAL SERVER ERROR";
        }
    }

    public static Boolean isAdmin(String adminId, String password){
        if (adminId != null && adminId.equals(Admin.ADMIN_ID)){
            if(password.equals(Admin.ADMIN_PASSWORD)){
                return true;
            }
        }
        return false;
    }


}