package ru.raimbek.rakhimbekov.alphabattle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raimbek.rakhimbekov.alphabattle.dto.AnalyticInfo;
import ru.raimbek.rakhimbekov.alphabattle.helpers.ExternalPaymentsHelper;
import ru.raimbek.rakhimbekov.alphabattle.models.Payment;
import ru.raimbek.rakhimbekov.alphabattle.models.UserSummary;
import ru.raimbek.rakhimbekov.alphabattle.services.PaymentService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class AnalyticController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/analytic", produces = "application/json")
    public String hello() {
        final List<Payment> payments = ExternalPaymentsHelper.getPaymentsFromFile();
        paymentService.saveAll(payments);

        for (Payment payment : paymentService.findAll()) {
            System.out.println(payment.getDesc());
        }

        final Set<String> userIds = payments.stream().map(Payment::getUserId).collect(Collectors.toSet());
        final List<List<UserSummary>> list = userIds.stream()
                .map(id -> groupByCategory(payments, id))
                .collect(Collectors.toList());

        return GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays()
                .setPrettyPrinting()
                .create()
                .toJson(list);
    }

    private static Double categorySummary(List<Payment> payments, String userId) {
        return payments.stream()
                .filter(p -> p.getUserId().equals(userId))
                .map(Payment::getAmount)
                .reduce(0., Double::sum);
    }

    private static List<UserSummary> groupByCategory(List<Payment> payments, String userId) {
        return payments.stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.groupingBy(Payment::getCategoryId))
                .entrySet()
                .stream()
                .map(e -> {
                    final List<Payment> userPayments = e.getValue();
                    final UserSummary userSummary = new UserSummary();
                    userSummary.setUserId(userId);
                    userSummary.setTotalSum(categorySummary(payments, userId));
                    userSummary.getAnalyticInfo().put(e.getKey(), new AnalyticInfo(
                            userPayments.stream().map(Payment::getAmount).min(Double::compareTo).get(),
                            userPayments.stream().map(Payment::getAmount).max(Double::compareTo).get(),
                            userPayments.stream().map(Payment::getAmount).reduce(0., Double::sum)
                    ));
                    return userSummary;
                })
                .collect(Collectors.toList());
    }
}
