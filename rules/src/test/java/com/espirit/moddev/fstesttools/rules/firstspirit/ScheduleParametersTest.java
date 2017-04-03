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

package com.espirit.moddev.fstesttools.rules.firstspirit;

import com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule.ScheduleParameters;

import de.espirit.firstspirit.access.schedule.DeployTask;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * SchedulerConfiguration Tester.
 */
public class ScheduleParametersTest {

    private ScheduleParameters testling;

    @Before
    public void before() throws Exception {
        testling = new ScheduleParameters("MyProject", "Full Generation");
    }

    /**
     * Method: isGenerateDeleteDirectory().
     */
    @Test
    public void testIsGenerateDeleteDirectory() throws Exception {
        assertTrue("Default true", testling.isGenerateDeleteDirectory());
    }

    /**
     * Method: setGenerateDeleteDirectory(final boolean generateDeleteDirectory).
     */
    @Test
    public void testSetGenerateDeleteDirectory() throws Exception {
        testling.setGenerateDeleteDirectory(false);

        assertFalse("set to false", testling.isGenerateDeleteDirectory());
    }

    /**
     * Method: getDeployTaskType().
     */
    @Test
    public void testGetDeployTaskType() throws Exception {
        assertThat("expect default", testling.getDeployTaskType(), is(DeployTask.Type.Full));
    }

    /**
     * Method: setDeployTaskType(final DeployTask.Type deployTaskType).
     */
    @Test
    public void testSetDeployTaskType() throws Exception {
        testling.setDeployTaskType(DeployTask.Type.Incremental);

        assertThat("set incremental", testling.getDeployTaskType(), is(DeployTask.Type.Incremental));
    }

    /**
     * Method: getGenerateUrlPrefix().
     */
    @Test
    public void testGetGenerateUrlPrefix() throws Exception {
        assertThat("expect default", testling.getGenerateUrlPrefix(), is("http://$address$"));
    }

    /**
     * Method: setGenerateUrlPrefix(final String generateUrlPrefix).
     */
    @Test
    public void testSetGenerateUrlPrefix() throws Exception {
        testling.setGenerateUrlPrefix("http://www.e-spirit.com");

        assertThat("expect new address", testling.getGenerateUrlPrefix(), is("http://www.e-spirit.com"));
    }

} 
