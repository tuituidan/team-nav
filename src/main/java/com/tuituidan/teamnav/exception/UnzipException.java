package com.tuituidan.teamnav.exception;

import com.tuituidan.teamnav.util.StringExtUtils;

import ch.qos.logback.classic.spi.EventArgUtil;

/**
 * UnzipException.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/8
 */
public class UnzipException extends RuntimeException {
    private static final long serialVersionUID = -1150086217385159231L;

    /**
     * UnzipException.
     *
     * @param message message
     * @param args    args
     */
    public UnzipException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }
}
