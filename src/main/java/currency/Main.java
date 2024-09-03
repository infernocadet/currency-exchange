package currency;

import currency.gui.Frame;
import currency.user.Role;
import currency.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Main instance;
    public User loggedInUser;
    public List<User> users = new ArrayList<>();

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void loadUsers() {
        String userDatabase = "src/main/resources/users.json";
        JSONParser parser = new JSONParser();
        try {
             JSONArray userArray = (JSONArray) parser.parse(new FileReader(userDatabase));
             for (Object userObject : userArray) {
                 JSONObject userJSON = (JSONObject) userObject;
                 String username = (String) userJSON.get("username");
                 String password = (String) userJSON.get("password");
                 String role = (String) userJSON.get("role");
                 users.add(new User(username, password, Role.valueOf(role)));
             }
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public Main() {
        instance = this;
        loadUsers();
        new Frame();
    }

    public static void main(String[] args) {
        new Main();
    }
}