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

import de.espirit.firstspirit.access.ClientScriptContext;
import de.espirit.firstspirit.access.User;
import de.espirit.firstspirit.access.project.Group;
import de.espirit.firstspirit.access.store.IDProvider;
import de.espirit.firstspirit.access.store.StoreElement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * The type Mocking client script context.
 */
public class MockingClientScriptContext extends MockingProjectScriptContext implements ClientScriptContext {

    private final IDProvider element = getMock(IDProvider.class);
    private final StoreElement storeElement = getMock(StoreElement.class);
    private final User user = getMock(User.class);
    private final List<Group> groups = new LinkedList<>();

    /**
     * Instantiates a new Mocking client script context.
     *
     * @param supportedEnvironments the supported environments
     */
    public MockingClientScriptContext(final Env... supportedEnvironments) {
        super(supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking client script context.
     *
     * @param locale                the locale
     * @param supportedEnvironments the supported environments
     */
    public MockingClientScriptContext(final Locale locale, final Env... supportedEnvironments) {
        super(locale, supportedEnvironments);
    }

    /**
     * Instantiates a new Mocking client script context.
     *
     * @param testLocale              the test locale
     * @param enableServiceBrokerFake the enable service broker fake
     * @param environment             the environment
     */
    public MockingClientScriptContext(final Locale testLocale, final boolean enableServiceBrokerFake, final Env environment) {
        super(testLocale, enableServiceBrokerFake, environment);
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Group[] getUserGroups() {
        return groups.toArray(new Group[groups.size()]);
    }

    /**
     * Add a group.
     *
     * @param group the group
     * @return the boolean
     */
    public boolean addGroup(final Group group) {
        return groups.add(group);
    }

    /**
     * Remove a group.
     *
     * @param group the group
     * @return the boolean
     */
    public boolean removeGroup(final Group group) {
        return groups.remove(group);
    }

    /**
     * Add groups.
     *
     * @param c the c
     * @return the boolean
     */
    public boolean addAllGroups(final Collection<Group> c) {
        return groups.addAll(c);
    }

    /**
     * Remove groups.
     *
     * @param c the c
     * @return the boolean
     */
    public boolean removeAllGroups(final Collection<Group> c) {
        return groups.removeAll(c);
    }

    @Override
    public StoreElement getStoreElement() {
        return storeElement;
    }

    @Override
    public IDProvider getElement() {
        return element;
    }
}
