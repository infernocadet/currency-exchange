package currency.user;

import java.util.Objects;

public class User {

    private String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        if (username == null || password == null || role == null) {
            throw new NullPointerException("Username, password, and role cannot be null");
        }
            this.username = username;
            this.password = password;
            this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role);
    }
}
