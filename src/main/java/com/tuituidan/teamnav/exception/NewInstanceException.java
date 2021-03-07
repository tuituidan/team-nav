package com.tuituidan.teamnav.exception;

import com.tuituidan.teamnav.util.StringExtUtils;

import ch.qos.logback.classic.spi.EventArgUtil;

/**
 * NewInstanceException.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/27
 */
public class NewInstanceException extends RuntimeException {
    private static final long serialVersionUID = -4429513569174786724L;

    /**
     * WrapperException.
     *
     * @param message message
     * @param args    args
     */
    public NewInstanceException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }
}
