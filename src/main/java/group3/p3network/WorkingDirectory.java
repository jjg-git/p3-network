package group3.p3network;

import java.io.File;

public class WorkingDirectory {
    private static volatile WorkingDirectory instance;
    private static final Object lock = new Object();

    public static WorkingDirectory getInstance() {
        WorkingDirectory result = instance;

        if (result == null) {
            synchronized (lock) {
                result = instance;
                if (result == null) {
                    instance = result = new WorkingDirectory();
                }
            }
        }

        return result;
    }

    public String getCurrent() {
        return new File("").getAbsolutePath();
    }
}
