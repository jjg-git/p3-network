package group3.p3network;

import java.nio.file.FileSystems;
import java.nio.file.Files;

public class CheckDirectory {
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
