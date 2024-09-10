import currency.user.User;
import currency.user.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorAndGetters() {
        User regularUser = new User("john_doe", "password123", Role.USER);
        User adminUser = new User("admin_user", "adminPass", Role.ADMIN);

        assertEquals("john_doe", regularUser.getUsername());
        assertEquals("password123", regularUser.getPassword());
        assertEquals(Role.USER, regularUser.getRole());

        assertEquals("admin_user", adminUser.getUsername());
        assertEquals("adminPass", adminUser.getPassword());
        assertEquals(Role.ADMIN, adminUser.getRole());
    }

    @Test
    void testNullUsername() {
        assertThrows(NullPointerException.class, () -> new User(null, "password", Role.USER));
    }

    @Test
    void testNullPassword() {
        assertThrows(NullPointerException.class, () -> new User("username", null, Role.USER));
    }

    @Test
    void testNullRole() {
        assertThrows(NullPointerException.class, () -> new User("username", "password", null));
    }

    @Test
    void testEmptyUsername() {
        User user = new User("", "password", Role.USER);
        assertEquals("", user.getUsername());
    }

    @Test
    void testEmptyPassword() {
        User user = new User("username", "", Role.USER);
        assertEquals("", user.getPassword());
    }

    @Test
    void testEqualUsers() {
        User user1 = new User("john", "pass", Role.USER);
        User user2 = new User("john", "pass", Role.USER);
        User user3 = new User("jane", "pass", Role.USER);

        assertEquals(user1, user1);
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());
    }

    @Test
    void testHashCode() {
        User user1 = new User("john", "pass", Role.USER);
        User user2 = new User("john", "pass", Role.USER);
        User user3 = new User("jane", "pass", Role.USER);

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}