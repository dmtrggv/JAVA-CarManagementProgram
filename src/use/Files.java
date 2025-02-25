package use;

import java.nio.file.Paths;

public class Files {

    // Get working directory
    public static String getFileDirectory() {
        String root = System.getProperty("user.dir");
        return Paths.get(root, "files").toString();
    }

}
