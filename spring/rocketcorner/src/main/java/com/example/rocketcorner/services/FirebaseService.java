package com.example.rocketcorner.services;

import com.example.rocketcorner.objects.Product;
import com.example.rocketcorner.objects.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

//    USER SERVICES

    public String saveUserDetails(User user) throws InterruptedException, ExecutionException {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference docRef = dbFirestore.collection("users").document();
            ApiFuture<WriteResult> collectionsApiFuture = docRef.set(user);

            return docRef.getId().toString();
        } catch (Exception e){
            throw e;
        }
    }

    public String updateUserDetails(String userId, HashMap<String, Object> updates) throws InterruptedException, ExecutionException {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference IDdocRef = dbFirestore.collection("users").document(userId);

            DocumentSnapshot docSnap = IDdocRef.get().get();
            if (docSnap.exists()) {
                ApiFuture<WriteResult> collectionsApiFuture = IDdocRef.update(updates);
                return IDdocRef.getId().toString();
            } else {
                return null;
            }
        } catch (Exception e){
            throw e;
        }
    }

    public String getUserId(String username) throws ExecutionException, InterruptedException {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference usersCollection = dbFirestore.collection("users");

            Query query = usersCollection.whereEqualTo("username", username);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            if(querySnapshot.get().getDocuments().size() == 0){
                return null;
            }

            return querySnapshot.get().getDocuments().get(0).getId().toString();
        } catch (Exception e){
            throw e;
        }
    }

    public HashMap<String, User> getAllUsers() throws ExecutionException, InterruptedException {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> queryFuture = dbFirestore.collection("users").get();
            List<QueryDocumentSnapshot> documents = queryFuture.get().getDocuments();

            HashMap<String, User> response = new HashMap<>();
            for (QueryDocumentSnapshot document: documents) {
                String userID = document.getId();
                User userObject = document.toObject(User.class);
                response.put(userID, userObject);
            }
            return response;
        } catch (Exception e){
            throw e;
        }
    }

    public HashMap<String, User> getUser (String userId) throws ExecutionException, InterruptedException {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<DocumentSnapshot> result = dbFirestore.collection("users").document(userId).get();


            DocumentSnapshot docSnap = (DocumentSnapshot) result.get();
            if(docSnap.exists()) {
                HashMap<String, User> response = new HashMap<>();
                try {
                    String userID = result.get().getId();
                    User userObject = result.get().toObject(User.class);
                    response.put(userID, userObject);
                    return response;
                } catch (Exception e) {
                    throw e;
                }
            }

            return null;
        } catch (Exception e){
            throw e;
        }
    }

    public boolean deleteUser (String userId) throws ExecutionException, InterruptedException {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference docRef = dbFirestore.collection("users").document(userId);
            DocumentSnapshot docSnap = docRef.get().get();

            if(docSnap.exists()) {
                ApiFuture<WriteResult> deleteResult = docRef.delete();
                return true;
            }
            return false;

        } catch (Exception e){
            throw e;
        }
    }

//    PRODUCT SERVICES

    public  String saveProductDetails(Product prod) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("products").document();
        ApiFuture<WriteResult> collectionsApiFuture = docRef.set(prod);

        return docRef.getId().toString();
    }

    public  String getProductId(String prodName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference usersCollection = dbFirestore.collection("products");
        Query query = usersCollection.whereEqualTo("name", prodName);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        if(querySnapshot.get().getDocuments().size() == 0){
            return null;
        }
        return querySnapshot.get().getDocuments().get(0).getId().toString();
    }

    public  String deleteProduct(String prodId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> deleteResult = dbFirestore.collection("products").document(prodId).delete();

        return deleteResult.get().getUpdateTime().toString();
    }

    public HashMap<String, Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> queryFuture = dbFirestore.collection("products").get();
        List<QueryDocumentSnapshot> documents = queryFuture.get().getDocuments();

        HashMap<String, Product> response = new HashMap<>();
        for (QueryDocumentSnapshot document: documents) {
            String productID = document.getId();
            Product userObject = document.toObject(Product.class);
            response.put(productID, userObject);
        }
        return response;
    }
}
