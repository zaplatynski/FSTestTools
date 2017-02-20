package com.espirit.moddev.fstesttools.rules.firstspirit.utils.context;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.agency.SpecialistType;
import de.espirit.firstspirit.agency.SpecialistsBroker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The type TestContext simulates a BaseContext for test environments.
 *
 * @author Zaplatynski
 */
public class TestContext implements BaseContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestContext.class);
    private final SpecialistsBroker broker;

    /**
     * Instantiates a new Test context.
     *
     * @param broker the broker
     */
    public TestContext(SpecialistsBroker broker) {
        if (broker == null) {
            throw new IllegalArgumentException("broker can not be null!");
        }
        this.broker = broker;
    }

    @Override
    public void logDebug(String s) {
        LOGGER.debug(s);
    }

    @Override
    public void logInfo(String s) {
        LOGGER.info(s);
    }

    @Override
    public void logWarning(String s) {
        LOGGER.warn(s);
    }

    @Override
    public void logError(String s) {
        LOGGER.error(s);
    }

    @Override
    public void logError(String s, Throwable throwable) {
        LOGGER.error(s, throwable);
    }

    @Override
    public boolean is(Env env) {
        switch (env) {
            case PREVIEW:
            case HEADLESS:
                return true;
            case WEBEDIT:
            case DROP:
            case FS_BUTTON:
            default:
                return false;
        }
    }

    @Override
    public <S> S requestSpecialist(SpecialistType<S> type) {
        return broker.requestSpecialist(type);
    }

    @Override
    public <S> S requireSpecialist(SpecialistType<S> type) {
        return broker.requireSpecialist(type);
    }
}
