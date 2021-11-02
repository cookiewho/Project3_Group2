package com.example.rocketcorner.firebase;

import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith( SpringRunner.class )
@SpringBootTest
public class FirebaseServiceTest {
    @Autowired
    FirebaseService firebaseService;

    Object testUserId;

    @Test
    public void insertUser() throws ExecutionException, InterruptedException {
        User newUser = new User("testUser", "email@test.com", "securePassword");
        List res = firebaseService.saveUserDetails(newUser);

        assertNotNull(res.get(0));
        assertNotNull(res.get(1));
        testUserId = res.get(1);
    }

    @Test
    public void getUser() throws ExecutionException, InterruptedException {
        String userId = firebaseService.getUserId("testUser");

        TimeUnit.SECONDS.sleep(5);

        assertEquals(testUserId.toString(), userId);
    }


    @Test
    public void deleteUser() throws ExecutionException, InterruptedException {
        String time = firebaseService.deleteUser(testUserId.toString());

        assertNotNull(time);
    }
}
