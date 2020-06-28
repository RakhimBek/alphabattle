package ru.raimbek.rakhimbekov.alphabattle.controller;

import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raimbek.rakhimbekov.alphabattle.dto.AnalyticInfo;
import ru.raimbek.rakhimbekov.alphabattle.helpers.ExternalPaymentsHelper;
import ru.raimbek.rakhimbekov.alphabattle.dto.Payment;
import ru.raimbek.rakhimbekov.alphabattle.dto.UserSummary;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class AnalyticController {

    @GetMapping(value = "analytic", produces = "application/json")
    public String hello() {
        final List<Payment> payments = ExternalPaymentsHelper.getPaymentsFromFile();
        final Set<String> userIds = payments.stream().map(Payment::getUserId).collect(Collectors.toSet());
        final List<List<UserSummary>> list = userIds.stream()
                .map(id -> groupByCategory(payments, id))
                .collect(Collectors.toList());

        return GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays()
                .setPrettyPrinting()
                .create()
                .toJson(list);
    }

    private static List<UserSummary> groupByCategory(List<Payment> payments, String userId) {
        final List<Payment> userPayments = payments.stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.toList());

        final Double total = userPayments.stream().map(Payment::getAmount).reduce(0., Double::sum);
        return userPayments.stream()
                .collect(Collectors.groupingBy(Payment::getCategoryId))
                .entrySet()
                .stream()
                .map(e -> {
                    final List<Payment> categoryPayments = e.getValue();
                    final UserSummary userSummary = new UserSummary();
                    userSummary.setUserId(userId);
                    userSummary.setTotalSum(total);
                    userSummary.getAnalyticInfo().put(e.getKey(), new AnalyticInfo(
                            categoryPayments.stream().map(Payment::getAmount).min(Double::compareTo).get(),
                            categoryPayments.stream().map(Payment::getAmount).max(Double::compareTo).get(),
                            categoryPayments.stream().map(Payment::getAmount).reduce(0., Double::sum)
                    ));
                    return userSummary;
                })
                .collect(Collectors.toList());
    }
}
