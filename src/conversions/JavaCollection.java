package conversions;

import java.util.Arrays;
import java.util.List;

public class JavaCollection {
    public List<String> createStringList() {
        return Arrays.asList("1", "2", "3");
    }

    public List<String> returnSame(List<String> s) {
        return s;
    }
}
