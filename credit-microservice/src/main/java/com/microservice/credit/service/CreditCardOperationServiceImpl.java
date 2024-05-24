package com.microservice.credit.service;

import com.microservice.credit.entity.CreditCardOperation;
import com.microservice.credit.exception.RequestException;
import com.microservice.credit.repository.CreditCardOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardOperationServiceImpl implements ICreditCardOperationService{

    @Autowired
    private CreditCardOperationRepository creditCardOperationRepository;

    @Override
    public List<CreditCardOperation> findAll() {
        return creditCardOperationRepository.findAll();
    }

    @Override
    public CreditCardOperation findCreditCardOperationById(Long id) {
        Optional<CreditCardOperation> creditCardOperation = creditCardOperationRepository.findOneById(id);

        if (creditCardOperation.isEmpty())
        {
            throw new RequestException("There is not a credit card operation fot the id: "+id);
        }

        return creditCardOperation.get();
    }
}
