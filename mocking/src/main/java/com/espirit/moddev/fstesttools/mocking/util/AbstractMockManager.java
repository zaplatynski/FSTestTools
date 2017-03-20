package com.espirit.moddev.fstesttools.mocking.util;

import de.espirit.common.base.Logging;
import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.agency.SpecialistType;
import de.espirit.firstspirit.agency.UIAgent;
import de.espirit.firstspirit.webedit.WebeditUiAgent;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The type AbstractMockManager hides utility code.
 */
public abstract class AbstractMockManager implements BaseContext {

    private final Map<Class, Object> mocks;
    protected final EnumSet<Env> supportedEnvironments;

    /**
     * Instantiates a new Abstract mock manager.
     *
     * @param supportedEnvironments the supported environments
     */
    public AbstractMockManager(final Env... supportedEnvironments) {
        if (supportedEnvironments == null || supportedEnvironments.length == 0) {
            throw new IllegalArgumentException("Please provide at least one environment!");
        }
        mocks = new HashMap<>();
        this.supportedEnvironments = EnumSet.copyOf(Arrays.asList(supportedEnvironments));
    }

    private static boolean isPreview(EnumSet<Env> supportedEnvironments) {
        return supportedEnvironments.contains(Env.PREVIEW);
    }

    private static boolean isContentCreator(EnumSet<Env> supportedEnvironments) {
        return supportedEnvironments.contains(Env.WEBEDIT);
    }

    /**
     * Prevent that wrong UiAgents can be obtained.
     *
     * @param <S>  the type parameter
     * @param type the type
     * @return the boolean
     */
    protected final <S> boolean preventThatWrongUiAgentIsRetuned(SpecialistType<S> type) {
        final boolean noUiAgentInCC = isContentCreator(supportedEnvironments) && Objects.equals(UIAgent.TYPE, type);
        final boolean noWebUiAgentInSA = isSiteArchitect() && Objects.equals(WebeditUiAgent.TYPE, type);
        return noUiAgentInCC || noWebUiAgentInSA;
    }

    private boolean isSiteArchitect() {
        return isPreview(supportedEnvironments) && !isContentCreator(supportedEnvironments);
    }

    /**
     * Gets or creates a mock.
     *
     * @param <S>          the type parameter
     * @param genericClass the generic class
     * @return the getMock
     */
    protected final <S> S getMock(final Class<? extends S> genericClass) {
        final Object o = mocks.get(genericClass);
        if (o == null) {
            Logging.logInfo("Create getMock for '" + genericClass.getSimpleName() + "'...", getClass());
            final S mock = Mockito.mock(genericClass);
            mocks.put(genericClass, mock);
            return mock;
        }
        return (S) o;
    }

    /**
     * Set up default mocks according to environment.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    protected final void setupDefaultMocks(final Locale locale, final Env[] supportedEnvironments) {
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
}