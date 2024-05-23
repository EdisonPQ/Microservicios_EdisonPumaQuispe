package com.microservice.credit.service;

import com.microservice.credit.dto.*;
import com.microservice.credit.entity.Credit;
import com.microservice.credit.entity.CreditCard;
import com.microservice.credit.entity.CreditPayment;
import com.microservice.credit.factory.CreditFactory;
import com.microservice.credit.factory.CreditPaymentFactory;
import com.microservice.credit.repository.CreditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CreditServiceImpl implements ICreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Override
    public List<Credit> findAll() {
        return creditRepository.findAll();
    }

    @Override
    public Credit findCreditById(Long id) {
        Optional<Credit> creditOptional = creditRepository.findById(id);

        return creditOptional.orElse(null);
    }

    @Override
    public Object storeCredit(CreditStoreRequestDto creditStoreRequestDto) {
        Float loanAmount = creditStoreRequestDto.getLoanAmount();
        String startDate = creditStoreRequestDto.getStartDate();
        String endDate = creditStoreRequestDto.getEndDate();
        Float interestRate = creditStoreRequestDto.getInterestRate();
        Long clientId = creditStoreRequestDto.getClientId();

        Credit credit = new CreditFactory().createCredit(loanAmount, startDate, endDate, interestRate, clientId);

        return creditRepository.save(credit);
    }

    @Override
    public PaymentCreditDebtResponseDto makeDebtPayment(PaymentCreditDebtRequestDto paymentCreditDebtRequestDto) {
        Long creditId = paymentCreditDebtRequestDto.getId();

        Optional<Credit> creditOptional = creditRepository.findOneById(creditId);

        if (creditOptional.isEmpty())
            return new PaymentCreditDebtResponseDto(false, "Operación fallida, el identificador de la cuenta no existe.", null);

        Credit credit = creditOptional.get();

        if (paymentCreditDebtRequestDto.getAmount() > credit.getAmount() - credit.getAmountPaid())
            return new PaymentCreditDebtResponseDto(false, "Operación fallida, el monto supera la deuda.", credit);

        Float amount = paymentCreditDebtRequestDto.getAmount();

        CreditPayment creditPayment = new CreditPaymentFactory().createCreditPayment(amount, credit);
        credit.setAmountPaid(credit.getAmountPaid() + amount);
        credit.setUpdatedAt(Timestamp.from(Instant.now()));
        credit.getPayments().add(creditPayment);

        creditRepository.save(credit);

        Optional<Credit> updatedCredit = creditRepository.findOneById(credit.getId());

        return new PaymentCreditDebtResponseDto(true, "Operación exitosa, se pagaron S/ " + amount, updatedCredit.orElse(null));
    }
}
