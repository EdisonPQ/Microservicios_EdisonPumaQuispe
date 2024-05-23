package com.microservice.credit.service;

import com.microservice.credit.entity.CreditPayment;
import com.microservice.credit.repository.CreditPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditPaymentServiceImpl implements ICreditPaymentService {

    @Autowired
    private CreditPaymentRepository creditPaymentRepository;

    @Override
    public List<CreditPayment> findAll() {
        return creditPaymentRepository.findAll();
    }

    @Override
    public CreditPayment findCreditPaymentById(Long id) {
        Optional<CreditPayment> creditPayment = creditPaymentRepository.findOneById(id);

        return creditPayment.orElse(null);
    }
}
