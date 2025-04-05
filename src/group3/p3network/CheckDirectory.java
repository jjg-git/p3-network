package group3.p3network;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;

public class CheckDirectory {
    public boolean check(String path) {
        // Check if it's a valid format directory
        // Source: https://regex101.com/r/xCywRh/1

        // if (!(path.matches(".+((?=\\\\)|(?=\\/)/?)"))) {
        //     return false;
        // }

        try {
            if (!Files.isDirectory(FileSystems.getDefault().getPath(path))) {
                System.out.println("Path is not a directory!");
                return false;
            }
        }
        catch (InvalidPathException e) {
            System.out.println("Invalid path format!");
            return false;
        }

        return true;
    }
}
