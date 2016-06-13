package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.agency.LanguageAgent;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by Zaplatynski on 30.11.2015.
 */
public abstract class AbstractSetupMocksStrategy implements SetupMocksStrategy {

    /**
     * The Context.
     */
    protected final MockingBaseContext context;
    private final Locale locale;
    private Language language;

    /**
     * Instantiates a new Abstract setup mocks strategy.
     *
     * @param context the context
     * @param locale  the locale
     */
    public AbstractSetupMocksStrategy(final MockingBaseContext context, final Locale locale) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        if (locale == null) {
            throw new IllegalArgumentException("locale is null");
        }
        this.context = context;
        this.locale = locale;
        mocksLanguageAgent();
    }

    private void mocksLanguageAgent() {
        final LanguageAgent agent = context.requestSpecialist(LanguageAgent.TYPE);
        final Language localLanguage = mockLanguage();
        when(agent.getMasterLanguage()).thenReturn(localLanguage);
        when(agent.getMetaLanguage()).thenReturn(localLanguage);
        when(agent.getLanguages()).thenReturn(Arrays.asList(localLanguage));
        when(agent.getEditorialLanguages()).thenReturn(Arrays.asList(localLanguage));
    }

    /**
     * Mock language.
     *
     * @return the language
     */
    protected Language mockLanguage() {
        if (language == null) {
            language = context.getMock(Language.class);
            reset(language);
            when(language.getLocale()).thenReturn(locale);
            when(language.isMasterLanguage()).thenReturn(Boolean.TRUE);
            when(language.getAbbreviation()).thenReturn(locale.getLanguage());
            when(language.getDisplayName((Language) any())).then(new Answer<String>() {
                @Override
                public String answer(final InvocationOnMock invocation) throws Throwable {
                    final Language mylanguage = (Language) invocation.getArguments()[0];
                    return locale.getDisplayLanguage(mylanguage.getLocale());
                }
            });
        }
        return language;
    }

}
