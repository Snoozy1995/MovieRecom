package movierecsys.dal;

import movierecsys.be.User;
import movierecsys.bll.exception.MovieRecSysException;
import movierecsys.bll.util.MovieSearcher;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserDAOTest {

    @Test
    public void testUserCreate() throws IOException { // fix ioexception in search to remove ioexception here @todo
        System.out.println("createUser");
        User user = UserDAO.createUser("TestName");
        UserDAO.deleteUser(user);
        assertNotNull("Assert that the user has been created.", user);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUserDelete() throws IOException { // fix ioexception in search to remove ioexception here @todo
        System.out.println("deleteUser");
        User user = UserDAO.createUser("TestName");
        UserDAO.deleteUser(user);
        assertTrue("Assert that the user has been removed.",(UserDAO.getUser(user.getId())!=null));
    }
}
