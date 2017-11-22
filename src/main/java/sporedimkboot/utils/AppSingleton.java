package sporedimkboot.utils;

public class AppSingleton {
    private static AppSingleton instance = null;

    public static AppSingleton getInstance() {
        if(instance == null) {
            instance = new AppSingleton();
        }
        return instance;
    }

}
