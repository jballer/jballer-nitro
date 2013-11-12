import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

import controllers.Security;

public class BasicTest extends UnitTest {

    @Test
    public void createUser() {
    	User.deleteAll();
        User.registerUser("bob@gmail.com", "secret");
        assertEquals(1, User.findAll().size());
        
        assertNotNull(User.connect("bob@gmail.com", "secret"));
        assertNull(User.connect("bad@user.name", "secret"));
        assertNull(User.connect("bob@gmail.com", "badpassword"));
        
    }

}
