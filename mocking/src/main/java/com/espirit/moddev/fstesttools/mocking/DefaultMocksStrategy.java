package com.espirit.moddev.fstesttools.mocking;

import java.util.Locale;


/**
 * The type Default mocks strategy.
 */
public class DefaultMocksStrategy extends AbstractSetupMocksStrategy {

    /**
     * Instantiates a new Default mocks strategy.
     *
     * @param context the context
     * @param locale  the locale
     */
    public DefaultMocksStrategy(final MockingBaseContext context, final Locale locale) {
        super(context, locale);
    }

    @Override
    public void setupMocks() {
        mockLanguage();
    }
}
