package com.ufrn.pertindetu.base.utils.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for standardized logging across the application.
 * Provides methods to log messages at different levels.
 */
public class LogUtils {

    /** Logger for this utility class itself */
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);

    /**
     * Logs a debug message using the given logger.
     *
     * @param logger  the target logger
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void debug(Logger logger, String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    /**
     * Logs an info message using the given logger.
     *
     * @param logger  the target logger
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void info(Logger logger, String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    /**
     * Logs a warn message using the given logger.
     *
     * @param logger  the target logger
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void warn(Logger logger, String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }

    /**
     * Logs an error message using the given logger.
     *
     * @param logger  the target logger
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void error(Logger logger, String message, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(message, args);
        }
    }

    /**
     * Logs a trace message using the given logger.
     *
     * @param logger  the target logger
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void trace(Logger logger, String message, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(message, args);
        }
    }

    /**
     * Logs a debug message using the internal LOGGER of this class.
     *
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void debug(String message, Object... args) {
        debug(LOGGER, message, args);
    }

    /**
     * Logs an info message using the internal LOGGER of this class.
     *
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void info(String message, Object... args) {
        info(LOGGER, message, args);
    }

    /**
     * Logs a warn message using the internal LOGGER of this class.
     *
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void warn(String message, Object... args) {
        warn(LOGGER, message, args);
    }

    /**
     * Logs an error message using the internal LOGGER of this class.
     *
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void error(String message, Object... args) {
        error(LOGGER, message, args);
    }

    /**
     * Logs a trace message using the internal LOGGER of this class.
     *
     * @param message the message to log
     * @param args    optional arguments for the message
     */
    public static void trace(String message, Object... args) {
        trace(LOGGER, message, args);
    }
}

