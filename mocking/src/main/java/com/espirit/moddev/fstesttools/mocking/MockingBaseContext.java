package com.espirit.moddev.fstesttools.mocking;

import de.espirit.common.base.Logging;
import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.access.ServicesBroker;
import de.espirit.firstspirit.agency.SpecialistType;
import de.espirit.firstspirit.agency.UIAgent;
import de.espirit.firstspirit.webedit.WebeditUiAgent;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The type Mocking context.
 */
public class MockingBaseContext implements BaseContext {

    private final Logger logger; //NOSONAR
    private final Map<Class, Object> mocks;
    private final EnumSet<Env> supportedEnvironments;
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
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null!");
        }
        if (supportedEnvironments == null || supportedEnvironments.length == 0) {
            throw new IllegalArgumentException("Please provide at least one environment!");
        }
        this.logger = LoggerFactory.getLogger(getClass());
        this.supportedEnvironments = EnumSet.copyOf(Arrays.asList(supportedEnvironments));
        this.enableServiceBrokerFake = enableServiceBrokerFake;
        mocks = new HashMap<>();
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

    private void setupDefaultMocks(final Locale locale, final Env[] supportedEnvironments) {
        SetupMocksStrategy strategy = new DefaultMocksStrategy(this, locale);
        for (final Env environment : supportedEnvironments) {
            switch (environment) {
                case WEBEDIT:
                    strategy = new WebEditMocksStrategy(this, locale);
                    break;
                case PREVIEW:
                    strategy = new PreviewMocksStrategy(this, locale);
                    break;
                default:
                    strategy = new DefaultMocksStrategy(this, locale);
            }
        }
        strategy.setupMocks();
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
        if (enableServiceBrokerFake && genericClass == ServicesBroker.class) {
            return (S) serviceBroker;
        }
        if(supportedEnvironments.contains(Env.WEBEDIT) && UIAgent.TYPE == type){
            //simulate that JavaClient does not support WebEdit aka ContentCreator Agents
            return null;
        }
        if(supportedEnvironments.contains(Env.PREVIEW) && !supportedEnvironments.contains(Env.WEBEDIT)  && WebeditUiAgent.TYPE == type){
            //simulate that ContentCreator does not support JavaClients aka SiteArchitect Agents
            return null;
        }
        return getMock(genericClass);
    }

    /**
     * Gets mock.
     *
     * @param genericClass the generic class
     * @return the getMock
     */
    <S> S getMock(final Class<? extends S> genericClass) {
        final Object o = mocks.get(genericClass);
        if (o == null) {
            Logging.logInfo("Create getMock for '" + genericClass.getSimpleName() + "'...", getClass());
            final S mock = Mockito.mock(genericClass);
            mocks.put(genericClass, mock);
            return mock;
        }
        return (S) o;
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
