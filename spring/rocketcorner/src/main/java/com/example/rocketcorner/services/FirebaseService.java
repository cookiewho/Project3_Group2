package com.example.rocketcorner.services;

import com.example.rocketcorner.objects.Product;
import com.example.rocketcorner.objects.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    public String saveUserDetails(User user) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("users").document();
        ApiFuture<WriteResult> collectionsApiFuture = docRef.set(user);

        return docRef.getId().toString();
    }

    public String getUserId(String username) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference usersCollection = dbFirestore.collection("users");
        Query query = usersCollection.whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        if(querySnapshot.get().getDocuments().size() == 0){
            return null;
        }
        return querySnapshot.get().getDocuments().get(0).getId().toString();
    }

    public String deleteUser (String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> deleteResult = dbFirestore.collection("users").document(userId).delete();

        return deleteResult.get().getUpdateTime().toString();
    }

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
}
