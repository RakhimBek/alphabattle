package ru.raimbek.rakhimbekov.alphabattle.services;

import org.springframework.stereotype.Service;
import ru.raimbek.rakhimbekov.alphabattle.dto.AnalyticInfo;
import ru.raimbek.rakhimbekov.alphabattle.models.Payment;
import ru.raimbek.rakhimbekov.alphabattle.repositories.PaymentRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Resource
    private PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        final List<Payment> list = new ArrayList<>();
        for (Payment payment : paymentRepository.findAll()) {
            list.add(payment);
        }
        return list;
    }

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    public void saveAll(List<Payment> payments) {
        paymentRepository.saveAll(payments);
    }

    public List<AnalyticInfo> categorySummary(String userId) {
        final List<Object> objects = paymentRepository.totalCategoryAmounts(userId);
        final List<AnalyticInfo> list= new ArrayList<>();
        for (Object o : objects) {
            final Double[] os = (Double[]) o;
            list.add(new AnalyticInfo(os[0], os[1], os[2]));
        }

        return list;
    }

    public Integer summary(String userId) {
        return paymentRepository.totalAmount(userId);
    }
}
