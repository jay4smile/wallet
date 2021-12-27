# wallet

This service handles below REST services and also uses H2 in memory database to handle the transactions.

## INSTALLATION STEP

```bash
mvn clean install
mvn spring-boot:run
```

## DOCKER STEP

```bash
sudo docker pull jay4smile/wallet
sudo docker run -p 8080:8080 jay4smile/wallet:1.0.0
```

## APIs
- **POST**: /api/wallet/transaction

API to credit or debit the amount in the wallet.

```json
{
    "user": "jay123",
    "transactionId": "0005",
    "transactionType": "CREDIT",
    "amount": 3000
}
```
```json
{
    "user": "jay123",
    "transactionId": "0004",
    "transactionType": "DEBIT",
    "amount": 3000
}
```
- **GET**: api/wallet/{userId}/{transactionType}

API to get the final balance of wallet of particular user

replace {userId} with **jay123** and {transactionType} with **BALANCE** OR **TRANSACTIONS** 

- in browser: /h2-console and use **password** as password to login and to check DB.

> ***kindly use **jay123** userId as it's active user. for negative testing use **jasmin123** ***
