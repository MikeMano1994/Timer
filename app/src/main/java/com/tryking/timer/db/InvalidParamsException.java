package com.tryking.timer.db;

/**
 * Created by Tryking on 2016/5/18.
 */
public class InvalidParamsException extends Exception {
    public InvalidParamsException(String detailMessage) {
        super(detailMessage);
    }
}
