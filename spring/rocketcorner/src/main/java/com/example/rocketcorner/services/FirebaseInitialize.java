package com.example.rocketcorner.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitialize {

    @PostConstruct
    public void initialize() throws IOException {
        try {
            FirebaseOptions options;
            if(System.getenv("FIREBASE_TYPE") == null) {
                FileInputStream serviceAccount =
                        new FileInputStream("./rocketcorner-85da2-firebase-adminsdk-hpsrm-414acd0134.json");

                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
            } else {
                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(createFirebaseCredential()))
                        .build();

                System.out.println("------- Firebase Initialized With Heroku -------");
            }

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream createFirebaseCredential() throws Exception {
        String privateKey = System.getenv("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");

        FirebaseCredential firebaseCredential = new FirebaseCredential();
        firebaseCredential.setType(System.getenv("FIREBASE_TYPE"));
        firebaseCredential.setProject_id(System.getenv("FIREBASE_PROJECT_ID"));
        firebaseCredential.setPrivate_key_id("FIREBASE_PRIVATE_KEY_ID");
        firebaseCredential.setPrivate_key(privateKey);
        firebaseCredential.setClient_email(System.getenv("FIREBASE_CLIENT_EMAIL"));
        firebaseCredential.setClient_id(System.getenv("FIREBASE_CLIENT_ID"));
        firebaseCredential.setAuth_uri(System.getenv("FIREBASE_AUTH_URI"));
        firebaseCredential.setToken_uri(System.getenv("FIREBASE_TOKEN_URI"));
        firebaseCredential.setAuth_provider_x509_cert_url(System.getenv("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
        firebaseCredential.setClient_x509_cert_url(System.getenv("FIREBASE_CLIENT_X509_CERT_URL"));

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(firebaseCredential);

        return IOUtils.toInputStream(jsonString);
    }

}