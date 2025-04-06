package group3.p3network;

public class Directory {
    private String currentDirectory;

    public Directory() {
        setToWorkingDirectory();
    }

    public Directory(String path) {
        if (!(new CheckDirectory()).check(path)) {
            setToWorkingDirectory();
            return;
        }

        currentDirectory = path;
    }

    private void setToWorkingDirectory() {
        currentDirectory = WorkingDirectory.getInstance().getCurrent();
    }

    private void setCurrentDirectory(String path) {
        if (!(new CheckDirectory()).check(path))
            return;

        currentDirectory = path;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }
}
