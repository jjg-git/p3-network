package group3.p3network;

import java.io.File;
import java.nio.file.*;

public class Directory {
    private String currentDirectory;

    public Directory() {
        setToWorkingDirectory();
    }

    private void setToWorkingDirectory() {
        currentDirectory = WorkingDirectory.getInstance().getCurrent();
    }

    public Directory(String path) {
        if (!(new CheckDirectory()).check(path)) {
            setToWorkingDirectory();
            return;
        }

        currentDirectory = path;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }
}
