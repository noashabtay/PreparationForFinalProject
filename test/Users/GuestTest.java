package Users;

import Asset.Player;
import org.junit.Rule;
import org.junit.Test;
import Exception.*;
import org.junit.rules.ExpectedException;
import system.DBController;
import system.SystemController;

import java.util.Date;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class GuestTest {
    Date birthdate=new Date(1993,10,12);
    SystemController controller = new SystemController("test controller");
    @Rule
    public final ExpectedException thrown= ExpectedException.none();

    public GuestTest() throws DontHavePermissionException, AlreadyExistException, MemberNotExist, IncorrectInputException {
    }

    @Test
    public void signInWithException() throws IncorrectInputException, AlreadyExistException, DontHavePermissionException {
        thrown.expect(AlreadyExistException.class);
        controller.signIn("noa","noa@gmail.com","123", birthdate);


        /*try to sign in but he is a member now*/
        controller.signIn("noa", "noa@gmail.com", "123", birthdate);
    }
    @Test
    public void signIn() throws IncorrectInputException, AlreadyExistException, DontHavePermissionException {
//        int sizeBefore = controller.getRoles().size();

        /*try to sign in with correct inputs  - result should be correct*/
        Member newMember = controller.signIn("noa", "noa2@gmail.com", "123", birthdate);
        assertNotNull(newMember);
        assertThat(newMember, instanceOf(Fan.class));
//        assertEquals(sizeBefore+1 , controller.getRoles().size());

    }
    @Test
    public void logInWithException() throws MemberNotExist, PasswordDontMatchException, IncorrectInputException, AlreadyExistException, DontHavePermissionException {
        thrown.expect(MemberNotExist.class);
//        int sizeBefore = controller.getRoles().size();

        controller.signIn("n","noa@gmail.com","123", birthdate);

        /*try to log in with not exist member - result should be negative*/
        Member member = controller.logIn("notExist@gmail.com","123");
        assertNull(member);
//        assertEquals(sizeBefore , controller.getRoles().size());

    }
    @Test
    public void logInIncorrectPassword() throws MemberNotExist, PasswordDontMatchException, IncorrectInputException, AlreadyExistException, DontHavePermissionException {
        thrown.expect(PasswordDontMatchException.class);
        controller.signIn("noa","noa@gmail.com","123", birthdate);
   //     int sizeBefore = controller.getRoles().size();

        /*try to log in with different password - result should be negative*/
        Member member = controller.logIn("noa@gmail.com","1223");
        assertNull(member);
//        assertEquals(sizeBefore , controller.getRoles().size());

    }
    @Test
    public void logIn() throws MemberNotExist, PasswordDontMatchException, IncorrectInputException, AlreadyExistException, DontHavePermissionException {
        Fan fan = (Fan)controller.signIn("noa","noa@gmail.com","123", birthdate);

        /*try to log in with correct details - result should be positive*/
        Member member = controller.logIn("noa@gmail.com","123");
        assertNotNull(member);
        assertEquals("noa",member.getName());
        assertThat(fan, instanceOf(Fan.class));
    }
}