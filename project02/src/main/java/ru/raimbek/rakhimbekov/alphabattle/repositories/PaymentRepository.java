package ru.raimbek.rakhimbekov.alphabattle.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.raimbek.rakhimbekov.alphabattle.dto.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    @Query("select min(amount), max(amount), sum(amount) from Payment where userId = ?1 group by categoryId ")
    List<Object> totalCategoryAmounts(String userId);

    @Query("select sum(amount) from Payment where userId = ?1")
    Integer totalAmount(String userId);

}
