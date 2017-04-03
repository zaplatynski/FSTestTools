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

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Log test method name rule.
 */
public class LogTestMethodNameRule extends TestWatcher {

    @Override
    protected void starting(Description description) {
        final Logger logger = LoggerFactory.getLogger(description.getTestClass());
        logger.info("Start of '{}'...", description.getMethodName());
    }

    @Override
    protected void succeeded(Description description) {
        final Logger logger = LoggerFactory.getLogger(description.getTestClass());
        logger.info("Successful termination of '{}'!", description.getMethodName());
    }

    @Override
    protected void failed(Throwable e, Description description) {
        final Logger logger = LoggerFactory.getLogger(description.getTestClass());
        logger.info("Termination with failure of '{}'!", description.getMethodName());
    }

    @Override
    protected void skipped(final AssumptionViolatedException e, final Description description) {
        final Logger logger = LoggerFactory.getLogger(description.getTestClass());
        logger.info("'{}' is ignored: " + e.getMessage(), description.getMethodName());
    }
}
