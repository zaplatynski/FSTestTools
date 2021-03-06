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

import de.espirit.common.util.Listable;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.Store.Type;
import de.espirit.firstspirit.client.plugin.contextmenu.ContextMenuContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * The Class MockingContextMenuContext.
 */
public class MockingContextMenuContext extends MockingBaseContext implements ContextMenuContext {


    private final Type storeType;
    private final Listable<IDProvider> list;
    private final List<IDProvider> elements;

    /**
     * Instantiates a new mocking context menu context.
     *
     * @param locale                  the locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param supportedEnvironments   the supported environments
     */
    public MockingContextMenuContext(Locale locale, boolean enableServiceBrokerFake, Env... supportedEnvironments) {
        this(locale, enableServiceBrokerFake, Type.SITESTORE, supportedEnvironments);
    }

    /**
     * Instantiates a new mocking context menu context.
     *
     * @param locale                  the locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param storeType               the store type
     * @param supportedEnvironments   the supported environments
     */
    public MockingContextMenuContext(Locale locale, boolean enableServiceBrokerFake, Type storeType, Env... supportedEnvironments) {
        super(locale, enableServiceBrokerFake, supportedEnvironments);
        this.storeType = storeType;
        elements = new ArrayList<>();
        list = new Listable<IDProvider>() {

            @Override
            public Iterator<IDProvider> iterator() {
                return elements.iterator();
            }

            @Override
            public List<IDProvider> toList() {
                return Collections.unmodifiableList(elements);
            }

            @Override
            public IDProvider getFirst() {
                if (elements.isEmpty()) {
                    return null;
                } else {
                    return elements.get(0);
                }
            }
        };
    }

    /**
     * Instantiates a new mocking context menu context.
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingContextMenuContext(Env... supportedEnvironments) {
        this(Locale.getDefault(), false, supportedEnvironments);
    }

    /**
     * Instantiates a new mocking context menu context.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingContextMenuContext(Locale locale, Env... supportedEnvironments) {
        this(locale, false, supportedEnvironments);
    }

    @Override
    public Type getStoreType() {
        return storeType;
    }

    @Override
    public Listable<IDProvider> getElements() {
        return list;
    }

    /**
     * Adds an element.
     *
     * @param element the IDProvider
     * @return true, if successful
     */
    public boolean addElement(IDProvider element) {
        return elements.add(element);
    }

    /**
     * Removes an element.
     *
     * @param element the IDProvider
     * @return true, if successful
     */
    public boolean removeElement(IDProvider element) {
        return elements.remove(element);
    }


}
