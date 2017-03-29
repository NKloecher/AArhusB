package test;

import model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void checkCorrectPassword() throws Exception {
        User user = new User("username", "password");
        assertTrue(user.checkPassword("password"));
    }

    @Test
    public void checkWrongPassword() throws Exception {
        User user = new User("username", "password");
        assertFalse(user.checkPassword("wrong"));
    }
}