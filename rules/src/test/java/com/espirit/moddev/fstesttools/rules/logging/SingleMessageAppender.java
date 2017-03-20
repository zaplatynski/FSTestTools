package com.espirit.moddev.fstesttools.rules.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The type Single message appender.
 */
public class SingleMessageAppender implements Appender {

    private ErrorHandler errorHandler;
    private Layout layout;
    private String name;
    private Filter filter;
    private String message;

    @Override
    public void addFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void clearFilters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        //empty
    }

    @Override
    public void doAppend(LoggingEvent event) {
        message = event.getRenderedMessage();
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    public Layout getLayout() {
        return layout;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
