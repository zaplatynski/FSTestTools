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

import de.espirit.firstspirit.client.plugin.report.ReportContext;

import java.util.Locale;


/**
 * The type Mocking report context.
 *
 * @param <T> the type parameter
 */
public class MockingReportContext<T> extends MockingBaseContext implements ReportContext<T> {

    private T object;
    private boolean repainted;

    /**
     * Instantiates a new Mocking report context.
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingReportContext(final Env... supportedEnvironments) {
        super(supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking report context.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingReportContext(final Locale locale, final Env... supportedEnvironments) {
        super(locale, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking report context.
     *
     * @param locale                  the locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param supportedEnvironments   the supported environments
     */
    public MockingReportContext(final Locale locale, final boolean enableServiceBrokerFake,
                                final Env... supportedEnvironments) {
        super(locale, enableServiceBrokerFake, supportedEnvironments);
    }



    @Override
    public T getObject() {
        return object;
    }

    /**
     * Sets object.
     *
     * @param object the object
     */
    public void setObject(final T object) {
        this.object = object;
    }

    @Override
    public void repaint() {
        repainted = true;
    }

    /**
     * Is repainted boolean.
     *
     * @return the boolean
     */
    public boolean isRepainted() {
        return repainted;
    }
}
