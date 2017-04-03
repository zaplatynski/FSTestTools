/*
 * ********************************************************************
 * Copyright (C) 2017 e-Spirit AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ********************************************************************
 */

package com.espirit.moddev.fstesttools.mocking.util;

import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.agency.LanguageAgent;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

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
    protected final AbstractMockManager context;
    private final Locale locale;
    private Language language;

    /**
     * Instantiates a new Abstract setup mocks strategy.
     *
     * @param context the context
     * @param locale  the locale
     */
    public AbstractSetupMocksStrategy(final AbstractMockManager context, final Locale locale) {
        this.context = Objects.requireNonNull(context, "context can not be null!");
        this.locale = Objects.requireNonNull(locale, "locale can not be null!");
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

    /**
     * Gets locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }
}
