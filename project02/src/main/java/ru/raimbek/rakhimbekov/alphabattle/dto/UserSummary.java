package ru.raimbek.rakhimbekov.alphabattle.dto;

import java.util.HashMap;
import java.util.Map;

public class UserSummary {
    private String userId;
    private Double totalSum;
    private Map<Double, AnalyticInfo> analyticInfo = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    public Map<Double, AnalyticInfo> getAnalyticInfo() {
        return analyticInfo;
    }
}
