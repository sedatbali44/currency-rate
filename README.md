# Currency Rate Conversion API

A Spring Boot application for currency exchange rate retrieval and conversion operations.

## Overview

This Currency Rate Conversion API provides a comprehensive solution for retrieving up-to-date exchange rates between different currencies and performing accurate currency conversions. The application leverages the ExchangeRate API to fetch real-time exchange rates and offers various endpoints for currency-related operations.

## Features

- **Exchange Rate Retrieval**: Get the latest exchange rate between any two supported currencies
- **Currency Conversion**: Calculate converted amounts between different currencies
- **Conversion History**: Track and query past conversion operations
- **Bulk Processing**: Upload CSV files for batch currency conversions
- **Flexible Search**: Filter conversion history by various parameters
- **Support for 170+ Currencies**: Comprehensive support for global currencies

## Technology Stack

- **Framework**: Spring Boot
- **Language**: Java
- **Database**: Configured for JPA/Hibernate
- **External API**: ExchangeRate API (v6)
- **Documentation**: Swagger UI (if implemented)

## API Endpoints

### Get Exchange Rate

Retrieves the current exchange rate between two currencies.

```
GET /api/v1/exchange/get-rate
```

**Parameters**:
- `sourceCurrency`: Source currency code (e.g., USD)
- `targetCurrency`: Target currency code (e.g., EUR)

**Example Request**:
```
GET http://localhost:8080/api/v1/exchange/get-rate?sourceCurrency=AWG&targetCurrency=AMD
```

**Example Response**:
```json
{
    "sourceCurrency": "AWG",
    "targetCurrency": "AMD",
    "rate": 215.3926,
    "timestamp": "2025-05-21T22:45:16.6274084"
}
```

### Calculate Conversion Amount

Converts an amount from one currency to another.

```
POST /api/v1/exchange/calculate-rate
```

**Request Body**:
```json
{
    "sourceCurrency": "USD",
    "targetCurrency": "EUR",
    "amount": 100.00
}
```

**Example Response**:
```json
{
    "sourceCurrency": "USD",
    "targetCurrency": "EUR",
    "rate": 0.92,
    "timestamp": "2025-05-21T23:15:20.123456",
    "message": "100.00 USD is equal to 92.00 EUR",
    "amount": 92.00
}
```

### Get Conversion History

Retrieves conversion history with flexible filtering options.

```
GET /api/v1/exchange/get-conversions
```

**Parameters**:
- `transactionId` (optional): Specific transaction ID
- `startDate` (optional): Filter by start date (ISO format)
- `endDate` (optional): Filter by end date (ISO format)
- `sourceCurrency` (optional): Filter by source currency
- `targetCurrency` (optional): Filter by target currency
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field and direction (e.g., transactionDate,desc)

**Example Request**:
```
GET http://localhost:8080/api/v1/exchange/get-conversions?startDate=2025-05-01T00:00:00&page=0&size=5&sort=transactionDate,desc
```

**Example Response**:
```json
{
    "content": [
        {
            "id": 1,
            "transactionId": "abcd1234",
            "sourceCurrency": "USD",
            "targetCurrency": "EUR",
            "sourceAmount": 100.00,
            "targetAmount": 92.00,
            "exchangeRate": 0.92,
            "transactionDate": "2025-05-21T23:15:20.123456",
            "reference": "USD-EUR"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "size": 5,
    "number": 0,
    "sort": {
        "empty": false,
        "unsorted": false,
        "sorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
}
```

### Get Conversion by Transaction ID

Retrieves a specific conversion by its transaction ID.

```
GET /api/v1/exchange/get-by-transactionId
```

**Parameters**:
- `transactionId`: The unique transaction identifier

**Example Request**:
```
GET http://localhost:8080/api/v1/exchange/get-by-transactionId?transactionId=64aa0c00
```

**Example Response**:
```json
{
    "id": 11,
    "transactionId": "64aa0c00",
    "sourceCurrency": "CHF",
    "targetCurrency": "TRY",
    "sourceAmount": 1250.00,
    "targetAmount": 58524.63,
    "exchangeRate": 46.819700,
    "transactionDate": "2025-05-21T21:36:46.593257",
    "reference": "CHF-TRY"
}
```

### Upload Conversions via CSV

Processes multiple currency conversions from a CSV file.

```
POST /api/v1/exchange/upload-conversions
```

**Parameters**:
- `file`: CSV file with conversion requests

**CSV Format**:
```
sourceCurrency;targetCurrency;amount
USD;EUR;100
GBP;JPY;500
```

**Example Response**:
```
CSV file processed successfully. 2 records saved.
```

## Setup and Installation

### Prerequisites

- Java 17 or later
- Maven 3.6 or later
- MySQL/PostgreSQL (or your preferred database)

### Configuration

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/currency-rate.git
   ```

2. Configure database connection in `application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/currency_db
   spring.datasource.username=root
   spring.datasource.password=password
   ```

3. Configure ExchangeRate API key in `ExchangeRateProviderServiceImpl.java` or through application properties:
   ```
   private static final String API_URL = "https://v6.exchangerate-api.com/v6/YOUR_API_KEY/latest/";
   ```

### Build and Run

```
mvn clean install
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.

## Error Handling

The API provides appropriate error responses in the following scenarios:

- Invalid currency codes
- Failed external API requests
- Invalid request parameters
- Invalid file format for CSV uploads

## Project Structure

- **Client**: Interfaces with external exchange rate API
- **Controllers**: API endpoints and request handling
- **DTOs**: Data Transfer Objects for API requests/responses
- **Entities**: Domain models and database entities
- **Repositories**: Data access interfaces
- **Services**: Business logic implementation
- **Exception**: Custom exception handling


# Sedat Bali
# https://github.com/sedatbali44