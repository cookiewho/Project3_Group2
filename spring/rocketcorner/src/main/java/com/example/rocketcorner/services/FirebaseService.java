package com.example.rocketcorner.services;

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

    public FirebaseService() {
    }

    public List saveUserDetails(User user) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("users").document();
        ApiFuture<WriteResult> collectionsApiFuture = docRef.set(user);

        List results = new ArrayList();
        results.add(collectionsApiFuture.get().getUpdateTime().toString());
        results.add(docRef.getId());
        return results;
    }

    public String getUserId(String username) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference usersCollection = dbFirestore.collection("users");
        Query query = usersCollection.whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        return querySnapshot.get().getDocuments().get(0).getId();
    }
}
