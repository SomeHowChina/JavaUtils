package src;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by haosong on 15-10-28.
 */
public class JavaCodeLineCounter {
    /**
     * use guava
     * */
    public int countCodeLines(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (!file.isDirectory()) {
            try {
                if (file.getName().endsWith("java")) {
                    List<String> strs = Files.readLines(file, Charsets.UTF_8);
                    return strs.size();
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }

        }
        File[] child = file.listFiles();
        if (child == null || child.length == 0) {
            return 0;
        }
        int count = 0;
        for (File f : child) {
            count += countCodeLines(f);
        }
        return count;
    }
}
