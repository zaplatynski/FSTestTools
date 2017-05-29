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

package com.espirit.moddev.fstesttools.rules.logging;

import org.apache.log4j.spi.RootLogger;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zaplatynski on 29.05.2017.
 */
public class FS2Log4JLoggerTest {

    private static Class<?> LOGGER =FS2Log4JLoggerTest.class;
    private static SingleMessageAppender appenderForTest = new SingleMessageAppender();
    private FS2Log4JLogger testling;

    @Before
    public void setUp() throws Exception {
        testling = new FS2Log4JLogger();
        RootLogger.getLogger(FS2Log4JLoggerTest.class).addAppender(appenderForTest);
    }

    @Test
    public void logTrace() throws Exception {
    }

    @Test
    public void logTrace1() throws Exception {
    }

    @Test
    public void logDebug() throws Exception {
    }

    @Test
    public void logDebug1() throws Exception {
    }

    @Test
    public void logInfo() throws Exception {
    }

    @Test
    public void logInfo1() throws Exception {
    }

    @Test
    public void logWarning() throws Exception {
    }

    @Test
    public void logWarning1() throws Exception {
    }

    @Test
    public void logError() throws Exception {
    }

    @Test
    public void logError1() throws Exception {
    }

    @Test
    public void logFatal() throws Exception {
    }

    @Test
    public void logFatal1() throws Exception {
    }

}
