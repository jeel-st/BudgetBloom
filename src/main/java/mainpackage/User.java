package mainpackage;

public class User {
    private static User instance;
    private String storedString;

    private User() {
        // Privater Konstruktor, um Instanziierung von außen zu verhindern
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void setLocalUser(String input) {
        storedString = input;
    }

    public String getLocalUser() {
        return storedString;
    }
}
