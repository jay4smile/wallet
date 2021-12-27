package com.jp.wallet.exception;

public class WalletException extends Exception {

    public WalletException() {
        super();
    }

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Throwable clause) {
        super(message, clause);
    }
}
