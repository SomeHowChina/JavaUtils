package src.JavaUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by haosong on 15-10-28.
 */
public class JavaCodeLineCounter {

    public static int countCodeLines(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (!file.isDirectory()) {
            try {
                if (file.getName().endsWith("java")) {
                    int count = 0;
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while (br.readLine() != null) {
                        ++count;
                    }
                    return count;
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
