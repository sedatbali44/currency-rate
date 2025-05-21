package com.currency_rate.currency_rate.Service;

import com.currency_rate.currency_rate.Dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExchangeRateService {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);

    ConversionResponse calculateRateAmount(ConversionRequest request);

    ResponseEntity<String> calculateConversions(MultipartFile file);

    List<ConversionRequest> parseCSV(MultipartFile file) throws IOException;
}