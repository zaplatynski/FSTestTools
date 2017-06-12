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
        final Locale locale = language.getLocale();
        when(uiAgent.getDisplayLanguage()).thenReturn(language);
        when(uiAgent.getPreviewLanguage()).thenReturn(language);
        when(uiAgent.getLocale()).thenReturn(locale);
        try {
            when(uiAgent.getLocale()).thenReturn(getLocale());
        } catch (NoSuchMethodError e) {
            // FS version smaller 5.2.609
        }
    }
}
