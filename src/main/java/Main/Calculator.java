package Main;

import java.util.List;

public class Calculator {
    public static double average(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }
}
