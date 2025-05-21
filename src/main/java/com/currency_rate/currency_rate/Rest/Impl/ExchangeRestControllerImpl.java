package com.currency_rate.currency_rate.Rest.Impl;

import com.currency_rate.currency_rate.Dto.*;
import com.currency_rate.currency_rate.Entity.ConversionHistory;
import com.currency_rate.currency_rate.Entity.Currency;
import com.currency_rate.currency_rate.Rest.ExchangeRestController;
import com.currency_rate.currency_rate.Service.ConversionHistoryService;
import com.currency_rate.currency_rate.Service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/exchange")
public class ExchangeRestControllerImpl implements ExchangeRestController {


    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ConversionHistoryService conversionHistoryService;



    @Override
    @GetMapping("/get-by-transactionId")
    public Optional<ConversionHistory> findByTransactionId(@RequestParam(required = true) String transactionId) {
        return conversionHistoryService.findByTransactionId(transactionId);
    }

    @Override
    @GetMapping("/get-rate")
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        return exchangeRateService.getExchangeRate(request);
    }

    @Override
    @PostMapping("/calculate-rate")
    public ConversionResponse calculateRateAmount(ConversionRequest request) {
        return exchangeRateService.calculateRateAmount(request);
    }

    @Override
    @GetMapping("/get-conversions")
    public Page<ConversionHistory> findConversions(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Currency sourceCurrency,
            @RequestParam(required = false) Currency targetCurrency,
            Pageable pageable
    ) {
        return conversionHistoryService.findConversions(transactionId, startDate, endDate, sourceCurrency, targetCurrency, pageable);
    }

    @Override
    @PostMapping("/upload-conversions")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        return exchangeRateService.calculateConversions(file);
    }





}
