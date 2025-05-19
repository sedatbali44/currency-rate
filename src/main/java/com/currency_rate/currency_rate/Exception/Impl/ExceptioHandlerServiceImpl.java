package com.currency_rate.currency_rate.Exception.Impl;


import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Exception.ExceptioHandlerService;
import org.springframework.stereotype.Service;

@Service
public class ExceptioHandlerServiceImpl implements ExceptioHandlerService{


    @Override
    public void validateCurrencyCode(Currency currency, String fieldName) {
        if (!Currency.isValid(currency.name())) {
            throw new InvalidCurrencyException("Invalid " + fieldName + ": " + currency);
        }
    }
}
