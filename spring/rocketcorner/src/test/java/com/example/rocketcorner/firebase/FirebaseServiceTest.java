package com.example.rocketcorner.firebase;

import com.example.rocketcorner.objects.Product;
import com.example.rocketcorner.objects.User;
import com.example.rocketcorner.services.FirebaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@RunWith( SpringRunner.class )
@SpringBootTest
public class FirebaseServiceTest {
    @Autowired
    FirebaseService firebaseService;

    @Test
    public void userFirebaseTests() throws ExecutionException, InterruptedException {
//      Creating a new user
        User newUser = new User("testUser", "email@test.com", "securePassword");
        String addedUserId = firebaseService.saveUserDetails(newUser);
        TimeUnit.SECONDS.sleep(5);
        //assert that we get a valid return
        assertNotNull(addedUserId);

//      Retreiving that user
        String userId = firebaseService.getUserId("testUser");
        TimeUnit.SECONDS.sleep(5);
        //assert that Ids match
        assertEquals(addedUserId, userId);

//      Updating user
        HashMap<String, Object> updates = new HashMap<>();

        updates.put("username", "NEWtestUser");
        updates.put("email", "NEWemail@test.com");
        updates.put("password", "NEWsecurePassword");

        String updatedUser_Id = firebaseService.updateUserDetails(userId, updates);
        TimeUnit.SECONDS.sleep(5);
        //assert that Ids match
        assertEquals(addedUserId, updatedUser_Id);
        assertEquals(userId, updatedUser_Id);

//      Get specific user
        HashMap<String, User> resp = firebaseService.getUser(userId);
        //assert that keys stayed the same
        resp.containsKey(userId);
        User gottenUser = resp.get(userId);
        //assert that user received matches what was entered
        assertEquals(gottenUser.getUsername(), "NEWtestUser");
        assertEquals(gottenUser.getEmail(), "NEWemail@test.com");
        assertEquals(gottenUser.getPassword(), "NEWsecurePassword");

//      Delete created user
        boolean userDeleted = firebaseService.deleteUser(userId);
        String deleted_userId = firebaseService.getUserId("testUser");

        assertTrue(userDeleted);
        assertNull(deleted_userId);
    }

    @Test
    public void productFirebaseTests() throws ExecutionException, InterruptedException {
//      Creating a new product
        Product newProd = new Product("testProd123456789", "Super useful test", "https://pm1.narvii.com/6752/d1bb17cb74d2d46355a957890c2e015b81c78165v2_hq.jpg", 34.99);
        String addedProductId = firebaseService.saveProductDetails(newProd);
        TimeUnit.SECONDS.sleep(5);
        //assert that we get a valid return
        assertNotNull(addedProductId);

//      Retrieving that product
        String prodId = firebaseService.getProductId("testProd123456789");
        TimeUnit.SECONDS.sleep(5);
        //assert that Ids match
        assertEquals(addedProductId, prodId);

//      Updating that product

        newProd.setName("name2");
        newProd.setDesc("Super duper useful test");
        newProd.setImgLink("https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Pok%C3%A9_Ball_icon.svg/1200px-Pok%C3%A9_Ball_icon.svg.png");
        newProd.setPrice(2);
        firebaseService.updateProductDetails(prodId, newProd);
        TimeUnit.SECONDS.sleep(5);
        HashMap<String, Product> myMap = firebaseService.getProduct(prodId);
        assertEquals(myMap.get(prodId).getName(), "name2");
        assertEquals(myMap.get(prodId).getDesc(), "Super duper useful test");
        assertEquals(myMap.get(prodId).getImgLink(), "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Pok%C3%A9_Ball_icon.svg/1200px-Pok%C3%A9_Ball_icon.svg.png");
        assertEquals(myMap.get(prodId).getPrice(), 2);

//      Delete created user
        String time = firebaseService.deleteProduct(prodId);
        TimeUnit.SECONDS.sleep(5);
        String deleted_prodId = firebaseService.getProductId("testProd123456789");
        TimeUnit.SECONDS.sleep(5);
        assertNotNull(time);
        assertNull(deleted_prodId);
    }


}
