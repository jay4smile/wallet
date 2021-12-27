package com.jp.wallet.exception;

public class DuplicateTransactionException extends Exception {

    public DuplicateTransactionException() {
       super();
    }

    public DuplicateTransactionException(String message) {
         super(message);
    }

    public DuplicateTransactionException(String message, Throwable throwable) {
         super(message, throwable);
    }
}
