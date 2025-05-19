package com.currency_rate.currency_rate.Exception;

import com.currency_rate.currency_rate.Entity.Currency;

public interface ExceptioHandlerService {

    void validateCurrencyCode(Currency currency, String fieldName);

}
