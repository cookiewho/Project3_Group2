package com.example.rocketcorner.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class FirebaseInitialize {

    @PostConstruct
    public void initialize() throws IOException {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("./rocketcorner-85da2-firebase-adminsdk-hpsrm-414acd0134.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://rocketcorner-85da2-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}