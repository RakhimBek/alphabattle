package ru.raimbek.rakhimbekov.alphabattle.dto;

public class AnalyticInfo {
    private final Double min;
    private final Double max;
    private final Double sum;

    public AnalyticInfo(Double min, Double max, Double sum) {
        this.min = min;
        this.max = max;
        this.sum = sum;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getSum() {
        return sum;
    }

}
