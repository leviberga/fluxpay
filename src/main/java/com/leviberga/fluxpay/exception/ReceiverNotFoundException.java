package com.leviberga.fluxpay.exception;

public class ReceiverNotFoundException extends RuntimeException {
    public ReceiverNotFoundException(String message) {
        super(message);
    }
}
