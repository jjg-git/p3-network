package group3.p3network;

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

    private class CheckDirectory {
        public boolean check(String path) {
            // Check if it's a valid format directory
            // Source: https://regex101.com/r/xCywRh/1

            if (!(path.matches(".+((?=\\\\)|(?=\\/)/?)"))) {
                return false;
            }

            if (!Files.isDirectory(FileSystems.getDefault().getPath(path))) {
                return false;
            }

            return true;
        }
    }

    private static class WorkingDirectory {
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
    }
}
