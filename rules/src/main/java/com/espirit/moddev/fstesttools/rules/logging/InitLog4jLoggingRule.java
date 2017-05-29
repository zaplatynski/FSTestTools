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

import de.espirit.common.base.Logging;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.rules.ExternalResource;

import java.util.Enumeration;


/**
 * The type Init log 4 j logging rule. <p>This rule sets up Log4J logging with a basic configuration so there will be output during tests.</p>
 */
public class InitLog4jLoggingRule extends ExternalResource {

    private static Logger logger;
    private boolean notConfigured;

    private Level rootLevel = Level.ALL;

    /**
     * Instantiates a new Init Log4J logging rule.
     */
    public InitLog4jLoggingRule() {
        this.notConfigured = !isLog4JConfigured();
    }

    /**
     * Instantiates a new Init Log4J logging rule.
     *
     * @param rootLevel the root log level
     */
    public InitLog4jLoggingRule(final Level rootLevel) {
        this();
        this.rootLevel = rootLevel;
    }

    /**
     * Returns true if it appears that log4j have been previously configured. This code checks to see if there are any appenders defined for log4j
     * which is the definitive way to tell if log4j is already initialized. See http://wiki.apache.org/logging-log4j/UsefulCode
     *
     * @return true if Log4J is configured, false otherwise.
     */
    private static boolean isLog4JConfigured() {
        final Enumeration appenders = Logger.getRootLogger().getAllAppenders();
        if (appenders.hasMoreElements()) {
            return true;
        } else {
            final Enumeration loggers = LogManager.getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                final Logger c = (Logger) loggers.nextElement();
                if (c.getAllAppenders().hasMoreElements()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void before() throws Throwable {
        if (notConfigured) {
            //Now configure Log4J with console appender
            BasicConfigurator.configure();
            System.out.println("Configure Log4J for basic output...");
            System.out.println("Set root LOGGER loglevel to '" + rootLevel.toString() + "'!");
            Logger.getRootLogger().setLevel(rootLevel);
        }
        if (logger == null) {
            Logger.getRootLogger().setAdditivity(false);
            logger = Logger.getLogger(InitLog4jLoggingRule.class);
            logger.setAdditivity(false);
        }
        //Init FirstSpirit Logging with special logger
        Logging.init(new FS2Log4JLogger());
    }

    @Override
    public void after() {
        if (!notConfigured) {
            logger.info("Reset basic Log4J configuration...");
            BasicConfigurator.resetConfiguration();
            notConfigured = true;
        }
    }

}
