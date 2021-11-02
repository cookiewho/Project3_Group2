package com.example.rocketcorner.firebase;

import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FirebaseServiceTest {
    @Autowired
    FirebaseService firebaseService;

    @Test
    public void insertUser() throws ExecutionException, InterruptedException {
        User newUser = new User("testUser", "email@test.com", "securePassword");
        List res = firebaseService.saveUserDetails(newUser);
        TimeUnit.SECONDS.sleep(5);

        String userId = firebaseService.getUserId("testUser");

        TimeUnit.SECONDS.sleep(5);

        assertEquals(res.get(1), userId);
    }
}
