package com.currency_rate.currency_rate.Exception.Impl;


import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Exception.ExceptionHandlerService;
import org.springframework.stereotype.Service;

@Service
public class ExceptionHandlerServiceImpl implements ExceptionHandlerService {


    @Override
    public void validateCurrencyCode(Currency currency, String fieldName) {
        if (!Currency.isValid(currency.name())) {
            throw new InvalidCurrencyException("Invalid " + fieldName + ": " + currency);
        }
    }
}
