package com.espirit.moddev.fstesttools.mocking;

import com.espirit.moddev.fstesttools.mocking.util.AbstractMockManager;

import de.espirit.firstspirit.access.ServicesBroker;
import de.espirit.firstspirit.agency.SpecialistType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;

/**
 * The type Mocking context.
 */
public class MockingBaseContext extends AbstractMockManager {

    private final Logger logger;
    private final MockingServiceBroker serviceBroker;
    private final boolean enableServiceBrokerFake;

    /**
     * Instantiates a new Mocking context with default locale.
     *
     * Example: new MockingContext(Arrays.asList(Env.WEBEDIT, Env.PREVIEW));
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingBaseContext(final Env... supportedEnvironments) {
        this(Locale.getDefault(), false, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking base context.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingBaseContext(final Locale locale, final Env... supportedEnvironments) {
        this(locale, false, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking context.
     *
     * Example: new MockingContext(Locale.GERMANY, Env.WEBEDIT, Env.PREVIEW);
     *
     * @param locale                  the locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param supportedEnvironments   the supported environments
     */
    public MockingBaseContext(final Locale locale, final boolean enableServiceBrokerFake, final Env... supportedEnvironments) {
        super(supportedEnvironments);
        Objects.requireNonNull(locale, "locale can not be null!");
        this.logger = LoggerFactory.getLogger(getClass());
        this.enableServiceBrokerFake = enableServiceBrokerFake;
        serviceBroker = new MockingServiceBroker();

        setupDefaultMocks(locale, supportedEnvironments);
    }

    /**
     * Is enable service broker fake.
     *
     * @return the boolean
     */
    public boolean isEnableServiceBrokerFake() {
        return enableServiceBrokerFake;
    }

    @Override
    public void logDebug(final String s) {
        logger.debug(s);
    }

    @Override
    public void logInfo(final String s) {
        logger.info(s);
    }

    @Override
    public void logWarning(final String s) {
        logger.warn(s);
    }

    @Override
    public void logError(final String s) {
        logger.error(s);
    }

    @Override
    public void logError(final String s, final Throwable throwable) {
        logger.error(s, throwable);
    }

    @Override
    public boolean is(final Env env) {
        return supportedEnvironments.contains(env);
    }

    @Override
    public <S> S requestSpecialist(final SpecialistType<S> type) {
        final ParameterizedType genericSuperclass = (ParameterizedType) type.getClass().getGenericSuperclass();
        final Type genericType = genericSuperclass.getActualTypeArguments()[0];
        final Class<S> genericClass = (Class<S>) genericType;
        if (enableServiceBrokerFake && Objects.equals(genericClass, ServicesBroker.class)) {
            return (S) serviceBroker;
        }
        if(preventThatWrongUiAgentIsRetuned(type)){
            return null;
        }
        return getMock(genericClass);
    }

    @Override
    public <S> S requireSpecialist(final SpecialistType<S> type) {
        final S specialist = requestSpecialist(type);
        if(specialist == null){
            throw new IllegalStateException("Specialist not available in environments: " + supportedEnvironments.toString());
        }
        return specialist;
    }
}
