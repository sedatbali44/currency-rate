package com.currency_rate.currency_rate.Rest.Impl;

import com.currency_rate.currency_rate.Dto.ExchangeRateRequest;
import com.currency_rate.currency_rate.Dto.ExchangeRateResponse;
import com.currency_rate.currency_rate.Repo.ExchangeRateRepository;
import com.currency_rate.currency_rate.Rest.ExchangeRestController;
import com.currency_rate.currency_rate.Service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/exchange")
public class ExchangeRestControllerImpl implements ExchangeRestController {


    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    @GetMapping("/get-rate")
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        return exchangeRateService.getExchangeRate(request);
    }

}
