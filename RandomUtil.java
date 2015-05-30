import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * user:hao.song
 * date:15/5/30
 * time:上午11:55
 */
public class RandomUtil {

    private static final char[] STRING_LIB_WITH_NUM = ("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890").toCharArray();

    private static final char[] STRING_LIB = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();

    /**
     * exclude end
     *
     * */
    public static int getRandomInt(int start, int end) {
        int diff = end - start;
        return ((int) (Math.random() * diff)) + start;
    }

    public static int getRandomInt(int end) {
        return getRandomInt(0, end);
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(STRING_LIB[getRandomInt(0, STRING_LIB.length)]);
        }
        return sb.toString();
    }

    public static String getRandomStringWithNum(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(STRING_LIB_WITH_NUM[getRandomInt(0, STRING_LIB_WITH_NUM.length)]);
        }
        return sb.toString();
    }

    public static <T> T getRandomObject(List<RandomObjectWithWeight<T>> randomList) {
        if (randomList == null || randomList.isEmpty()) {
            throw new RuntimeException("random list can not be empty");
        }
        double total = 0;
        NavigableMap<Double, T> randomMap = new TreeMap<Double, T>();
        for (RandomObjectWithWeight<T> obj : randomList) {
            if (obj.weight - 0 < 0.000001) {
                throw new RuntimeException("weight cannot be 0");
            }
            total += obj.weight;
            randomMap.put(total, obj.object);
        }
        double r = Math.random() * total;
        return randomMap.ceilingEntry(r).getValue();
    }

    public static class RandomObjectWithWeight<K> {
        private K object;
        private double weight;

        public RandomObjectWithWeight(K object, double weight) {
            this.object = object;
            this.weight = weight;
        }
    }
}
