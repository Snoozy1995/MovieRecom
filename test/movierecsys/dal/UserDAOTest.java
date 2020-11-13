package movierecsys.dal;

import movierecsys.be.User;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserDAOTest {

    @Test
    public void testUserCreate() {
        System.out.println("createUser");
        User user = UserDAO.createUser("TestName");
        UserDAO.deleteUser(user);
        assertNotNull("Assert that the user has been created.", user);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUserDelete() {
        System.out.println("deleteUser");
        User user = UserDAO.createUser("TestName");
        UserDAO.deleteUser(user);
        assertTrue("Assert that the user has been removed.",(UserDAO.getUser(user.getId())!=null));
    }
}
