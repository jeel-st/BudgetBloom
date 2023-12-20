package Singleton;

public class SingletonUser {
    private static SingletonUser instance;

    private String LocalUsername;

    private SingletonUser() {
        LocalUsername = null;
    }

    public static SingletonUser getInstance() {
        if (instance == null) {
            instance = new SingletonUser();
        }
        return instance;
    }

    public String getName() {
        return LocalUsername;
    }

    public void setName(String newName) {
        this.LocalUsername = newName;
    }
}
