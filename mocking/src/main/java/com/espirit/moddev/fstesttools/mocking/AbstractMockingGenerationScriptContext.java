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

package com.espirit.moddev.fstesttools.mocking;

import de.espirit.firstspirit.access.GenerationScriptContext;
import de.espirit.firstspirit.access.Language;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.agency.LanguageAgent;

import org.mockito.Mockito;

import java.util.Locale;


/**
 * The type Mocking generation script context.
 */
public abstract class AbstractMockingGenerationScriptContext extends MockingProjectScriptContext implements GenerationScriptContext {

    private final boolean preview;
    private final boolean release;
    private final TemplateSet templateSet;

    /**
     * Instantiates a new Mocking generation script context.
     *
     * @param locale                  the locale
     * @param preview                 the preview
     * @param release                 the release
     * @param enableServiceBrokerFake the enable service broker fake
     * @param supportedEnvironments   the supported environments
     */
    public AbstractMockingGenerationScriptContext(final Locale locale, final boolean preview, final boolean release,
                                                  final boolean enableServiceBrokerFake, final Env supportedEnvironments) {
        super(locale, enableServiceBrokerFake, supportedEnvironments);
        this.preview = preview;
        this.release = release;
        templateSet = Mockito.mock(TemplateSet.class);
    }


    @Override
    public Language getLanguage() {
        return requestSpecialist(LanguageAgent.TYPE).getMasterLanguage();
    }

    @Override
    public TemplateSet getTemplateSet() {
        return templateSet;
    }

    @Override
    public boolean isRelease() {
        return release;
    }

    @Override
    public boolean isPreview() {
        return preview;
    }

    @Override
    public String toString(final Object o) throws Exception {
        if (o != null) {
            return o.toString();
        }
        return "null";
    }
}
