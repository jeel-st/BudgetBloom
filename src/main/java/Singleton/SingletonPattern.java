package Singleton;

public class SingletonPattern {
    private static SingletonPattern instance;

    private String LocalUsername;

    private SingletonPattern() {
        LocalUsername = null;
    }

    public static SingletonPattern getInstance() {
        if (instance == null) {
            instance = new SingletonPattern();
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
