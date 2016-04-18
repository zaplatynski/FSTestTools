package com.espirit.moddev.fstesttools.rules.mocking;

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
    public WebEditMocksStrategy(final MockingBaseContext context, final Locale locale) {
        super(context, locale);
    }

    @Override
    public void setupMocks() {
        WebeditUiAgent uiAgent = context.requestSpecialist(WebeditUiAgent.TYPE);
        Language language = mockLanguage();
        when(uiAgent.getDisplayLanguage()).thenReturn(language);
        when(uiAgent.getPreviewLanguage()).thenReturn(language);
    }
}
