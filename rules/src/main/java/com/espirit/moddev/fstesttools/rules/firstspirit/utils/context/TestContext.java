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

package com.espirit.moddev.fstesttools.rules.firstspirit.utils.context;

import de.espirit.firstspirit.access.BaseContext;
import de.espirit.firstspirit.agency.SpecialistType;
import de.espirit.firstspirit.agency.SpecialistsBroker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


/**
 * The type TestContext simulates a BaseContext for test environments.
 *
 * @author e-Spirit AG
 */
public class TestContext implements BaseContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestContext.class);
    private final SpecialistsBroker broker;

    /**
     * Instantiates a new Test context.
     *
     * @param broker the broker
     */
    public TestContext(SpecialistsBroker broker) {
        this.broker = Objects.requireNonNull(broker, "SpecialistsBroker can not be null!");
    }

    @Override
    public void logDebug(String s) {
        LOGGER.debug(s);
    }

    @Override
    public void logInfo(String s) {
        LOGGER.info(s);
    }

    @Override
    public void logWarning(String s) {
        LOGGER.warn(s);
    }

    @Override
    public void logError(String s) {
        LOGGER.error(s);
    }

    @Override
    public void logError(String s, Throwable throwable) {
        LOGGER.error(s, throwable);
    }

    @Override
    public boolean is(Env env) {
        switch (env) {
            case PREVIEW:
            case HEADLESS:
                return true;
            case WEBEDIT:
            case DROP:
            case FS_BUTTON:
            default:
                return false;
        }
    }

    @Override
    public <S> S requestSpecialist(SpecialistType<S> type) {
        return broker.requestSpecialist(type);
    }

    @Override
    public <S> S requireSpecialist(SpecialistType<S> type) {
        return broker.requireSpecialist(type);
    }
}
