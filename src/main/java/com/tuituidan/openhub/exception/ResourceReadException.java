package com.tuituidan.openhub.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.tuituidan.openhub.util.StringExtUtils;

/**
 * ResourceReadException.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/3/6
 */
public class ResourceReadException extends RuntimeException {

    private static final long serialVersionUID = 143422262865091780L;

    /**
     * ResourceReadException.
     *
     * @param message message
     * @param args args
     */
    public ResourceReadException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }

}
