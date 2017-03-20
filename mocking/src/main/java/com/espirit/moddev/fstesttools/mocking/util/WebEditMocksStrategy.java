package com.espirit.moddev.fstesttools.mocking.util;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.webedit.WebeditUiAgent;

import java.util.Locale;

import static org.mockito.Mockito.when;


/**
 * The type Web edit mocks strategy.
 */
public class WebEditMocksStrategy extends AbstractSetupMocksStrategy {


    /**
     * Instantiates a new Web edit mocks strategy.
     *
     * @param context the context
     * @param locale  the locale
     */
    public WebEditMocksStrategy(final AbstractMockManager context, final Locale locale) {
        super(context, locale);
    }

    @Override
    @SuppressWarnings("squid:S1166")
    public void setupMocks() {
        final WebeditUiAgent uiAgent = context.requestSpecialist(WebeditUiAgent.TYPE);
        final Language language = mockLanguage();
        when(uiAgent.getDisplayLanguage()).thenReturn(language);
        when(uiAgent.getPreviewLanguage()).thenReturn(language);
        try {
            when(uiAgent.getLocale()).thenReturn(getLocale());
        } catch (NoSuchMethodError e) {
            // FS version smaller 5.2.609
        }
    }
}
